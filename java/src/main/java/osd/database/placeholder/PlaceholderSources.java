package osd.database.placeholder;

import osd.database.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Singleton
class PlaceholderSources {

    private final Supplier<Stream<Block>> blocks;
    private final Supplier<Stream<Professor>> professors;
    private final Supplier<Stream<Room>> rooms;
    private final Supplier<Stream<Course>> courses;

    @Inject
    PlaceholderSources(final PlaceholderConfig config) {
        blocks = config.getBlockParser();
        professors = config.getProfessorParser();
        rooms = config.getRoomParser();
        courses = config.getCourseParser();
    }

    public Stream<Section> getSections() {
        return courses.get().flatMap(c -> StreamSupport.stream(c.getSections().spliterator(), false));
    }

    public Stream<Professor> getProfessors() {
        return professors.get();
    }

    public Stream<Block> getBlocks() {
        return blocks.get();
    }

    public Stream<Room> getRooms() {
        return rooms.get();
    }
}
