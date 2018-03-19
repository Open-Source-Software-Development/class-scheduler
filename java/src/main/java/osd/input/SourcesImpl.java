package osd.input;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

class SourcesImpl implements Sources {

    private final Collection<Section> sections;
    private final Collection<Professor> professors;
    private final Collection<Block> blocks;
    private final Collection<Room> rooms;

    @Inject
    SourcesImpl(final Collection<Section> sections, final Collection<Professor> professors,
                final Collection<Block> blocks, final Collection<Room> rooms) {
        // Defensive-copy everything.
        this.sections = new ArrayList<>(sections);
        this.professors = new ArrayList<>(professors);
        this.blocks = new ArrayList<>(blocks);
        this.rooms = new ArrayList<>(rooms);
    }

    @Override
    public Stream<Section> getSections() {
        return sections.stream();
    }

    @Override
    public Stream<Professor> getProfessors() {
        return professors.stream();
    }

    @Override
    public Stream<Block> getBlocks() {
        return blocks.stream();
    }

    @Override
    public Stream<Room> getRooms() {
        return rooms.stream();
    }
}
