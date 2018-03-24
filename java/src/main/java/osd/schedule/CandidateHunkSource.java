package osd.schedule;

import osd.considerations.Lookups;
import osd.database.*;
import osd.output.Hunk;
import osd.util.relation.ManyToManyRelation;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Generates candidate hunks for sections. A {@code CandidateHunkSource}
 * instance initially knows (by checking the provided {@link Sources} and
 * {@link Constraints} what professors and rooms are available for each
 * section. As hunks are added to the schedule, combinations that are no longer
 * possible are removed from further consideration. This allows the algorithm
 * to efficiently generate candidate hunks.
 */
class CandidateHunkSource {

    final ManyToManyRelation<Section, Professor> professors;
    final ManyToManyRelation<Section, Room> rooms;
    private final BlockAvailability blockAvailability;
    private final Constraints constraints;
    private final Lookups lookups;
    private final int expectedHunks;

    CandidateHunkSource(final Sources sources, final Constraints constraints) {
        this.professors = new ManyToManyRelation<>();
        this.rooms = new ManyToManyRelation<>();
        this.blockAvailability = new BlockAvailability(sources.getBlocks().collect(Collectors.toList()));
        this.constraints = constraints;
        this.lookups = SchedulerLookups.empty();
        this.expectedHunks = (int)sources.getSections().count();

        // For each section, find all the professors and rooms that are
        // compatible with it.
        sources.getSections().distinct().forEach(section -> {
            sources.getProfessors().distinct()
                    .filter(Constraints.professorPredicate(constraints, section))
                    .forEach(professor -> professors.add(section, professor));
            sources.getRooms().distinct()
                    .filter(Constraints.roomPredicate(constraints, section))
                    .forEach(room -> rooms.add(section, room));
        });
    }

    // TODO: this could be encapsulated away
    CandidateHunkSource(final CandidateHunkSource rebind, final Lookups lookups) {
        this.professors = new ManyToManyRelation<>(rebind.professors);
        this.rooms = new ManyToManyRelation<>(rebind.rooms);
        this.blockAvailability = new BlockAvailability(rebind.blockAvailability);
        this.constraints = rebind.constraints;
        this.lookups = lookups;
        this.expectedHunks = rebind.expectedHunks;
    }

    /**
     * Removes every combination that is incompatible with some hunk. In
     * practice, this means that every combination with the same (room, block)
     * pair or the same (professor, block) pair becomes unavailable.
     * @param hunk the hunk being added
     */
    void onHunkAdded(final Hunk hunk) {
        final Section section = hunk.getSection();
        blockAvailability.setUnavailable(hunk);
        professors.remove(section);
        rooms.remove(section);
    }

    int getExpectedHunks() {
        return expectedHunks;
    }

    Stream<Hunk> getCandidateHunks(final Section section) {
        // This three-layered flatMap is less intimidating than it looks.
        // We start by getting the professors and rooms available for this
        // section, from which we can compute the blocks. That gives us the
        // four elements we need to write out our hunks.
        return getProfessors(section)
                .flatMap(professor ->
                        getRooms(section).flatMap(room ->
                                getBlocks(professor, room).map(blocks ->
                                        getHunk(section, professor, room, blocks))))
                .filter(Hunk::validateBlocks)
                .distinct()
                .filter(constraints.bindBaseConstraints(lookups));
    }

    private Stream<Professor> getProfessors(final Section section) {
        return professors.getOrDefault(section, Collections::emptySet).stream();
    }

    private Stream<Room> getRooms(final Section section) {
        return rooms.getOrDefault(section, Collections::emptySet).stream();
    }

    private Stream<Block> getBlocks(final Professor professor, final Room room) {
        return blockAvailability.getAvailable(professor, room);
    }

    private static Hunk getHunk(final Section section, final Professor professor,
                                final Room room, final Block block) {
        final List<Block> blocks = section.getBlockingStrategy()
                .apply(block)
                .collect(Collectors.toList());
        return new Hunk(section, professor, room, blocks);
    }

}
