package osd.schedule;

import osd.input.*;
import osd.output.Hunk;
import osd.util.relation.ManyToManyRelation;

import javax.inject.Inject;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Availability {

    private final ManyToManyRelation<Section, Professor> professors;
    private final ManyToManyRelation<Section, Room> rooms;
    private final BlockAvailability blockAvailability;

    @Inject
    Availability(final Sources sources, final Constraints constraints) {
        this.professors = new ManyToManyRelation<>();
        this.rooms = new ManyToManyRelation<>();
        this.blockAvailability = new BlockAvailability(sources.getBlocks().collect(Collectors.toList()));
        sources.getSections().distinct().forEach(s -> {
            sources.getProfessors().distinct()
                    .filter(Constraints.professorPredicate(constraints, s))
                    .forEach(p -> professors.add(s, p));
            sources.getRooms().distinct()
                    .filter(Constraints.roomPredicate(constraints, s))
                    .forEach(r -> rooms.add(s, r));
        });
    }

    Availability(final Availability copyOf) {
        this.professors = new ManyToManyRelation<>(copyOf.professors);
        this.rooms = new ManyToManyRelation<>(copyOf.rooms);
        this.blockAvailability = new BlockAvailability(copyOf.blockAvailability);
    }

    Stream<Section> getImpacted(final Hunk hunk) {
        final Section section = hunk.getSection();
        final Professor professor = hunk.getProfessor();
        final Room room = hunk.getRoom();
        return Stream.concat(
                professors.reversed().get(professor).stream(),
                rooms.reversed().get(room).stream()
        ).filter(s -> !section.equals(s)).unordered().distinct();
    }

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

    Stream<Professor> getProfessors(final Section section) {
        return professors.getOrDefault(section, Collections::emptySet).stream();
    }

    Stream<Room> getRooms(final Section section) {
        return rooms.getOrDefault(section, Collections::emptySet).stream();
    }

    Stream<Block> getBlocks(final Professor professor, final Room room) {
        return blockAvailability.getAvailable(professor, room);
    }

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
