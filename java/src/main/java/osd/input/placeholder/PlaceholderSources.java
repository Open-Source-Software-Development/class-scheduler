package osd.input.placeholder;

import osd.input.Sources;
import osd.input.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Singleton
class PlaceholderSources implements Sources {

    private final Supplier<Stream<Block>> blocks;
    private final Supplier<Stream<Professor>> professors;
    private final Supplier<Stream<Room>> rooms;
    private final Supplier<Stream<Course>> courses;

    @Inject
    public PlaceholderSources(final PlaceholderConfig config) {
        blocks = config.getBlockParser();
        professors = config.getProfessorParser();
        rooms = config.getRoomParser();
        courses = config.getCourseParser();
    }

    @Override
    public Stream<Section> getSections() {
        return courses.get().flatMap(c -> StreamSupport.stream(c.getSections().spliterator(), false));
    }

    @Override
    public Stream<Professor> getProfessors() {
        return professors.get();
    }

    @Override
    public Stream<Block> getBlocks() {
        return blocks.get();
    }

    @Override
    public Stream<Room> getRooms() {
        return rooms.get();
    }
}
