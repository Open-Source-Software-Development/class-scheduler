package osd.schedule;

import osd.database.Course;
import osd.database.Section;
import osd.database.Sources;
import osd.util.relation.OneToManyRelation;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SchedulerCandidate implements Results {

    private final SchedulerState state;
    private final OneToManyRelation<Long, Course> coursesByCandidateCount;
    private final Map<Course, Set<Section>> outstandingSections;

    private final int expectedHunkCount;

    // Tracks how many candidates the course with the fewest candidates
    // has, allowing us to efficiently find high-priority courses.
    private long lowestCandidateCount;

    /**
     * DI constructor. Instantiate a candidate schedule with no hunks, and
     * every section in the {@linkplain Sources sources} pending.
     * @param sources an {@link Sources} instance
     */
    @Inject
    SchedulerCandidate(final Sources sources, final Considerations considerations) {
        // Construct containers.
        this.state = new SchedulerState(sources, considerations);
        this.coursesByCandidateCount = new OneToManyRelation<>();
        this.outstandingSections = new HashMap<>();

        // Initially, every section is outstanding.
        sources.getSections().forEach(this::registerOutstandingSection);

        // Initialize the priority of each course.
        // Using outstandingSections.keySet() instead of sources.getSections()
        // is a "free" way of only computing priorities once per course.
        this.lowestCandidateCount = Integer.MAX_VALUE;
        updatePriorities(outstandingSections.keySet());
        expectedHunkCount = (int)sources.getSections().count();
    }

    // Copy-construct a candidate schedule and update its state. This MUST be called from
    // getNextGenerationCandidates(), and nowhere else.
    private SchedulerCandidate(final SchedulerCandidate copyOf, final SchedulerState withState) {
        System.err.println(withState.recentHunk);
        // Copy data, copy-construct containers.
        this.state = withState;
        this.coursesByCandidateCount = new OneToManyRelation<>(copyOf.coursesByCandidateCount);
        this.outstandingSections = new HashMap<>(copyOf.outstandingSections);
        this.expectedHunkCount = copyOf.expectedHunkCount;
        this.lowestCandidateCount = copyOf.lowestCandidateCount;

        // Extract section and course data, for convenience.
        final Hunk newHunk = withState.recentHunk;
        final Section newSection = newHunk.getSection();
        final Course newCourse = newSection.getCourse();

        // The new hunk's section is no longer outstanding.
        // We copy-construct the existing set so as to avoid
        // interfering with "upstream" generations, which would
        // disrupt backtracking.
        final Set<Section> oldSections = outstandingSections.get(newCourse);
        final Set<Section> newSections = new HashSet<>(oldSections);
        newSections.remove(newSection);

        // If the new hunk's section was the last section of its course,
        // remove the course from our outstanding sections map.
        // Otherwise, update that map to indicate that the section
        // is no longer outstanding.
        if (newSections.isEmpty()) {
            outstandingSections.remove(newCourse);
            coursesByCandidateCount.reversed().remove(newCourse);
        } else {
            outstandingSections.put(newCourse, newSections);
        }

        // Update the priorities of other sections as needed.
        final Set<Course> adjacent = state.getAdjacent(newCourse)
                .filter(outstandingSections::containsKey)
                .collect(Collectors.toSet());
        updatePriorities(adjacent);
    }

    private void updatePriorities(final Set<Course> courses) {
        final int total = courses.size();
        int complete = 0;
        for (final Course course: courses) {
            updatePriority(course);
            complete++;
            System.err.println("Initialized " + complete + "/" + total + " courses");
        }
    }

    /**
     * Stream the next generation of candidates. Find a section with the
     * smallest number of possible hunks, and ask our {@linkplain SchedulerState state}
     * for all the candidate hunks for that section. For each of those hunks,
     * generate a new candidate schedule by adding that hunk to this candidate.
     * @return the stream of next-gen candidate schedules
     */
    Stream<SchedulerCandidate> getNextGenerationCandidates() {
        final Section section = getHighPrioritySection();
        return state.childStatesForSection(section)
                .map(hunk -> new SchedulerCandidate(this, hunk));
    }

    /**
     * Determine if this candidate schedule is complete.
     * @return if this candidate schedule is complete
     */
    boolean isComplete() {
        return outstandingSections.isEmpty();
    }

    /**
     * Determine if this candidate schedule is impossible. A schedule is
     * "impossible" if at least one section has no candidates. A complete
     * candidate is never impossible.
     * @return if this candidate schedule is impossible
     */
    boolean isImpossible() {
        return !isComplete() && lowestCandidateCount == 0;
    }

    private void registerOutstandingSection(final Section section) {
        final Course course = section.getCourse();
        final Set<Section> set = outstandingSections.computeIfAbsent(course, unused -> new HashSet<>());
        set.add(section);
    }

    private void updatePriority(final Course forCourse) {
        assert outstandingSections.containsKey(forCourse);
        assert !outstandingSections.get(forCourse).isEmpty();
        // Priorities are defined per Course, but the
        // underlying State interface expects a Section.
        // Since we're only counting the number of possibilities,
        // we can pick any section of the course.
        final Section someSectionOfCourse = outstandingSections.get(forCourse).iterator().next();
        // Count up the candidates for the section.
        final long candidateCount = state.countCandidates(someSectionOfCourse);
        // Update the course's candidate count.
        coursesByCandidateCount.add(candidateCount, forCourse);
        // Update our own lowest candidate count, if appropriate.
        lowestCandidateCount = Math.min(lowestCandidateCount, candidateCount);
    }

    private Section getHighPrioritySection() {
        return outstandingSections.get(getHighPriorityCourse()).iterator().next();
    }

    private Course getHighPriorityCourse() {
        // Courses with lower candidate counts are higher priority.
        Set<Course> courses = coursesByCandidateCount.get(lowestCandidateCount);

        // Handle the case where we've scheduled all the courses
        // with that count and need to "refresh" our lowest count.
        if (courses == null) {
            lowestCandidateCount = outstandingSections.keySet().stream()
                    .mapToLong(course -> coursesByCandidateCount.reversed().get(course))
                    .min().orElseThrow(AssertionError::new);
            courses = coursesByCandidateCount.get(lowestCandidateCount);
        }

        return courses.iterator().next();
    }

    @Override
    public List<Hunk> getHunks() {
        return state.lookupAllHunks().collect(Collectors.toList());
    }

    @Override
    public int getExpectedHunkCount() {
        return expectedHunkCount;
    }
}

