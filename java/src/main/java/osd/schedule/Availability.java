package osd.schedule;

import osd.input.*;
import osd.output.Hunk;
import osd.util.relation.ManyToManyRelation;

import javax.inject.Inject;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Trackers for professor, room, and block availability. An {@code Availability}
 * instance initially knows (by checking {@linkplain Constraints constraints})
 * what professors and rooms are compatible for a section. As hunks are added
 * to the schedule, combinations that are no longer possible are removed from
 * further consideration. This allows the algorithms to efficiently generate
 * candidate hunks.
 */
class Availability {

    private final ManyToManyRelation<Section, Professor> professors;
    private final ManyToManyRelation<Section, Room> rooms;
    private final BlockAvailability blockAvailability;

    /**
     * DI constructor.
     * @param sources a {@link Sources} instance indicating everything available
     * @param constraints {@link Constraints} used to filter out impossible combinations
     */
    @Inject
    Availability(final Sources sources, final Constraints constraints) {
        this.professors = new ManyToManyRelation<>();
        this.rooms = new ManyToManyRelation<>();
        this.blockAvailability = new BlockAvailability(sources.getBlocks().collect(Collectors.toList()));

        // For each section, find all the professors and rooms that are
        // compatible with it.
        sources.getSections().distinct().forEach(s -> {
            sources.getProfessors().distinct()
                    .filter(Constraints.professorPredicate(constraints, s))
                    .forEach(p -> professors.add(s, p));
            sources.getRooms().distinct()
                    .filter(Constraints.roomPredicate(constraints, s))
                    .forEach(r -> rooms.add(s, r));
        });
    }

    /**
     * Copy constructor. After construction, the original and copy are
     * completely independent.
     * @param copyOf the instance to copy
     */
    Availability(final Availability copyOf) {
        this.professors = new ManyToManyRelation<>(copyOf.professors);
        this.rooms = new ManyToManyRelation<>(copyOf.rooms);
        this.blockAvailability = new BlockAvailability(copyOf.blockAvailability);
    }

    /**
     * Gets all the sections with a room or professor mentioned in the hunk.
     * Exception: The hunk's section itself is <em>not</em> included.
     * This lets the algorithm know what sections' priority is impacted by
     * adding that hunk.
     * @param hunk the hunk that's being added
     * @return the sections whose priority is impacted by that addition
     */
    Stream<Section> getImpacted(final Hunk hunk) {
        final Section section = hunk.getSection();
        final Professor professor = hunk.getProfessor();
        final Room room = hunk.getRoom();
        return Stream.concat(
                professors.reversed().get(professor).stream(),
                rooms.reversed().get(room).stream()
        ).filter(s -> !section.equals(s)).unordered().distinct();
    }

    /**
     * Removes every combination that is incompatible with some hunk. In
     * practice, this means that every combination with the same (room, block)
     * pair or the same (professor, block) pair becomes unavailable.
     * @param hunk the hunk being added
     */
    void onHunkAdded(final Hunk hunk) {
        final Section section = hunk.getSection();
        final Professor professor = hunk.getProfessor();
        final Room room = hunk.getRoom();
        final Block block = hunk.getBlock();
        blockAvailability.setUnavailable(block, room);
        blockAvailability.setUnavailable(block, professor);
        professors.remove(section);
        rooms.remove(section);
    }

    /**
     * Finds all the professors who can teach some section.
     * @param section the section to get professors for
     * @return a stream of professors who can teach that section
     */
    Stream<Professor> getProfessors(final Section section) {
        return professors.getOrDefault(section, Collections::emptySet).stream();
    }

    /**
     * Finds all the rooms where a section can be taught.
     * @param section the section to get rooms for
     * @return a stream of rooms where that section can be taught
     */
    Stream<Room> getRooms(final Section section) {
        return rooms.getOrDefault(section, Collections::emptySet).stream();
    }

    /**
     * Finds all the blocks where a professor and room are available.
     * @param professor the professor
     * @param room the room
     * @return a stream of all blocks where both are available
     */
    Stream<Block> getBlocks(final Professor professor, final Room room) {
        return blockAvailability.getAvailable(professor, room);
    }

    /**
     * Gets all the candidate hunks for a section. This is does <em>not</em>
     * consider preferences; the hunks are returned in an arbitrary order.
     * @param section the section to get candidate hunks for
     * @return a stream of candidate hunks for that section
     */
    Stream<Hunk> candidates(final Section section) {
        // This three-layered flatMap is less intimidating than it looks.
        // We start by getting the professors and rooms available for this
        // section, from which we can compute the blocks. That gives us the
        // four elements we need to write out our hunks.
        return getProfessors(section)
                .flatMap(p ->
                                getRooms(section).flatMap(r ->
                                        getBlocks(p, r)
                                                .map(b -> new Hunk(section, p, r, b))
                                )
                );
    }

}
