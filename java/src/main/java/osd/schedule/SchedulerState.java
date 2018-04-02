package osd.schedule;

import osd.database.*;
import osd.util.relation.ManyToManyRelation;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The state of a work-in-progress schedule. A scheduler state knows what
 * sections have been scheduled so far, and what possible combinations remain.
 * They provide access to new, more complete, schedules, using a section as a
 * key to where to start looking.
 * <p>Scheduler states come in two flavors. The first is a "root state", which
 * knows all legal possibilities before any scheduling has been done. The second
 * is a "child state", which is derived from another state. A child state
 * always has exactly one more hunk than its parent. Root states are created
 * directly from a {@linkplain Sources data source} and {@linkplain Considerations considerations}.
 * Child states are materialized from a {@linkplain #childStatesForSection(Section) stream}
 * for some section.</p>
 */
class SchedulerState implements Lookups {

    private final Considerations considerations;
    private final ManyToManyRelation<Course, Professor> professorsForCourse;
    private final ManyToManyRelation<Course, Room> roomsForCourse;

    private final ManyToManyRelation<Professor, Block> professorAvailableAt;
    private final ManyToManyRelation<Room, Block> roomAvailableAt;
    private final Map<Professor, Set<Hunk>> professorsTeaching;
    private final Map<Section, Hunk> hunks;

    /**
     * In child states, the hunk that was added to generate this child. Null
     * in root states.
     */
    final Hunk recentHunk;

    /**
     * Initialize a root state. A root state holds every possibility before
     * any actual scheduling has taken place. In order to construct those
     * possibilities, it needs {@linkplain Sources data sources}, and
     * {@linkplain Considerations constraints} to proactively eliminate
     * impossible combinations up-front.
     * @param sources section/professor/room/block data
     * @param considerations to extract user constraints
     */
    SchedulerState(final Sources sources, final Considerations considerations) {
        // Make note of our considerations.
        this.considerations = considerations;

        // Initialize all our containers.
        this.professorsForCourse = new ManyToManyRelation<>();
        this.roomsForCourse = new ManyToManyRelation<>();
        this.professorAvailableAt = new ManyToManyRelation<>();
        this.roomAvailableAt = new ManyToManyRelation<>();
        this.professorsTeaching = Collections.emptyMap();
        this.hunks = Collections.emptyMap();

        // Initialize room/professor availability per block,
        sources.getBlocks().forEach(block -> initBlock(sources, block));
        // and comparability per section.
        sources.getSections().forEach(section -> initSection(sources, section));

        // recentHunk is null on root states.
        recentHunk = null;
    }

    private SchedulerState(final SchedulerState copyOf, final Hunk withHunk) {
        // Directly copy members that don't change.
        this.considerations = copyOf.considerations;
        this.professorsForCourse = copyOf.professorsForCourse;
        this.roomsForCourse = copyOf.roomsForCourse;

        // Copy-construct members that do change.
        this.professorAvailableAt = new ManyToManyRelation<>(copyOf.professorAvailableAt);
        this.roomAvailableAt = new ManyToManyRelation<>(copyOf.roomAvailableAt);
        this.professorsTeaching = new HashMap<>(copyOf.professorsTeaching);
        this.hunks = new HashMap<>(copyOf.hunks);

        // Insert the new hunk.
        final Section section = withHunk.getSection();
        hunks.put(section, withHunk);
        this.recentHunk = withHunk;

        // Update professor and room availability.
        final Professor professor = withHunk.getProfessor();
        final Room room = withHunk.getRoom();
        final Set<Block> blocks = withHunk.getBlocks();
        for (final Block block : blocks) {
            professorAvailableAt.remove(professor, block);
            roomAvailableAt.remove(room, block);
        }
        professorsTeaching.computeIfAbsent(professor, unused -> new HashSet<>()).add(withHunk);
    }

    /**
     * Stream "child" states that gain a hunk for some section. A "child" state
     * inherits sources, considerations, preferences, and hunks from its parent,
     * but it also has some additional hunk.
     * <p>The child stream is sorted by the preference order of the new hunks.
     * Child sections having a high-scoring extra hunk come first.</p>
     * @param forSection the section to add hunks for
     * @return child states with hunks for that section
     * @see SchedulerState#countCandidates(Section)
     */
    Stream<SchedulerState> childStatesForSection(final Section forSection) {
        return candidates(forSection)
                .sorted(considerations.getPreferenceComparator(this))
                .map(hunk -> new SchedulerState(this, hunk));
    }

    /**
     * Determines how many candidates are available for a section. For any
     * section s, {@code countCandidates(s) == childStatesForSection(s).count()},
     * except this method doesn't worry about sorting and so has better
     * performance.
     * @param forSection the section to count candidates for
     * @return how many candidates there are for that section
     */
    long countCandidates(final Section forSection) {
        return candidates(forSection).count();
    }

    /**
     * Stream all the courses that share a room or professor with some course.
     * The course itself is excluded from the stream.
     * @param course the course to get adjacent courses for
     * @return the courses that share a room or professor with it
     */
    Stream<Course> getAdjacent(final Course course) {
        return Stream.concat(
                    getAdjacent0(course, professorsForCourse),
                    getAdjacent0(course, roomsForCourse))
                .filter(other -> !course.equals(other))
                .distinct();
    }

    private <T> Stream<Course> getAdjacent0(final Course course, final ManyToManyRelation<Course, T> relation) {
        return relation.get(course).stream()
                .map(value -> relation.reversed().get(value))
                .flatMap(Set::stream);
    }

    private Stream<Hunk> candidates(final Section section) {
        final Course course = section.getCourse();

        // Bind our constraints to the current scheduler state.
        final Predicate<Hunk> userConstraints = considerations.getUserConstraints();
        final Predicate<Hunk> baseConstraints = considerations.getBaseConstraints(this);

        // Hunks have four members: Course, professor, room, and block set.
        // Grab all the professors that can teach this section's course...
        return getProfessors(course).flatMap(professor ->
                // Grab all compatible rooms and generate (professor, room) pairs...
                getRooms(course).flatMap(room ->
                        // Use those pairs as keys into the block availability list...
                        getBlocks(professor, room).map(block ->
                                // And now we've got all four elements, so we can make hunks.
                                getHunk(section, professor, room, block))))
                // Filter out hunks with illegal blocks
                // (eg. a three-hour class scheduled past the end of the day).
                .filter(Hunk::validateBlocks)
                // Dedup and run constraints.
                .distinct()
                .filter(userConstraints)
                .filter(baseConstraints);
    }

    // Given a section, professor, room, and block, create a hunk, using the
    // section's course's blocking strategy to turn the block into a set.
    private static Hunk getHunk(final Section section, final Professor professor,
                                final Room room, final Block block) {
        final Course course = section.getCourse();
        final Set<Block> blocks = course.getBlockingStrategy()
                .apply(block).collect(Collectors.toSet());
        return new Hunk(section, professor, room, blocks);
    }

    private Stream<Professor> getProfessors(final Course forCourse) {
        return professorsForCourse.get(forCourse).stream();
    }

    private Stream<Room> getRooms(final Course forCourse) {
        return roomsForCourse.get(forCourse).stream();
    }

    private Stream<Block> getBlocks(final Professor professor, final Room room) {
        // Get all the blocks our room is available at.
        final Set<Block> roomAvailable = roomAvailableAt.getOrDefault(room, Collections::emptySet);
        // Same deal as above, but with professors.
        final Set<Block> professorAvailable = professorAvailableAt.getOrDefault(professor, Collections::emptySet);
        // Intersect those sets to find the blocks where both
        // the professor and the room are available.
        final Set<Block> result = new HashSet<>(professorAvailable);
        result.retainAll(roomAvailable);
        return result.stream();
    }

    private void initBlock(final Sources sources, final Block block) {
        // Each professor and room is initially available at every block.
        sources.getProfessors().forEach(professor -> professorAvailableAt.add(professor, block));
        sources.getRooms().forEach(room -> roomAvailableAt.add(room, block));
    }

    private void initSection(final Sources sources, final Section section) {
        // Initialize the professors and rooms for our section.
        // As an optimization, we apply user constraints, which always evaluate to the same thing
        // per hunk, up-front. This lets us weed out illegal combinations during program startup,
        // rather than every time we generate candidates. We can't apply constraints to a professor
        // or room directly, however, so we first wrap them in a hunk, filling in the other values
        // with null.
        final Predicate<Hunk> constraints = considerations.getUserConstraints();
        sources.getProfessors()
                .filter(professor -> constraints.test(new Hunk(section, professor, null, null)))
                .forEach(professor -> professorsForCourse.add(section.getCourse(), professor));
        sources.getRooms()
                .filter(room -> constraints.test(new Hunk(section, null, room, null)))
                .forEach(room -> roomsForCourse.add(section.getCourse(), room));
    }

    @Override
    public Stream<Hunk> lookupAllHunks() {
        return hunks.values().stream();
    }

    @Override
    public Stream<Hunk> lookup(final Professor professor) {
        if (professorsTeaching.containsKey(professor)) {
            return professorsTeaching.get(professor).stream();
        }
        // Prefer an empty stream to null.
        return Stream.empty();
    }

    @Override
    public Hunk lookup(final Section section) {
        return hunks.get(section);
    }
}
