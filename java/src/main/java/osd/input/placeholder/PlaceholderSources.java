package osd.input.placeholder;

import osd.input.Sources;
import osd.input.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class PlaceholderSources implements Sources {

    private final PlaceholderParser<Block> blocks;
    private final PlaceholderParser<Professor> professors;
    private final PlaceholderParser<Room> rooms;
    private final PlaceholderParser<Course> courses;

    @Inject
    public PlaceholderSources(final PlaceholderConfig config) {
        try {
            blocks = config.getBlockParser();
            professors = config.getProfessorParser();
            rooms = config.getRoomParser();
            courses = config.getCourseParser();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<Section> getSections() {
        return courses.stream().flatMap(c -> StreamSupport.stream(c.getSections().spliterator(), false));
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
