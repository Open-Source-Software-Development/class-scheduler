package osd.schedule;

import osd.considerations.Lookups;
import osd.input.Sources;
import osd.input.*;
import osd.output.Hunk;
import osd.util.relation.ManyToManyRelation;

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

    final ManyToManyRelation<Section, Professor> professors;
    final ManyToManyRelation<Section, Room> rooms;
    private final BlockAvailability blockAvailability;
    private final Constraints constraints;
    private final Lookups lookups;

    Availability(final Sources sources, final Constraints constraints) {
        this.professors = new ManyToManyRelation<>();
        this.rooms = new ManyToManyRelation<>();
        this.blockAvailability = new BlockAvailability(sources.getBlocks().collect(Collectors.toList()));
        this.constraints = constraints;
        this.lookups = SchedulerLookups.empty();

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

    Availability(final Availability rebind, final Lookups lookups) {
        this.professors = new ManyToManyRelation<>(rebind.professors);
        this.rooms = new ManyToManyRelation<>(rebind.rooms);
        this.blockAvailability = new BlockAvailability(rebind.blockAvailability);
        this.constraints = rebind.constraints;
        this.lookups = lookups;
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
    Stream<Hunk> getCandidateHunks(final Section section) {
        // This three-layered flatMap is less intimidating than it looks.
        // We start by getting the professors and rooms available for this
        // section, from which we can compute the blocks. That gives us the
        // four elements we need to write out our hunks.
        return getProfessors(section)
                .flatMap(p ->
                        getRooms(section).flatMap(r ->
                                getBlocks(p, r).map(b ->
                                        new Hunk(section, p, r, b))))
                .distinct()
                .filter(constraints.bindBaseConstraints(lookups));
    }

}
