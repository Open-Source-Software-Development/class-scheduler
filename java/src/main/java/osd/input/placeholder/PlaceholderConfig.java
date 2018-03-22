package osd.input.placeholder;

import osd.input.*;
import osd.main.Flags;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;

class PlaceholderConfig {

    private final String prefix;

    @Inject
    PlaceholderConfig(final Flags flags) {
        // Using these flags more for example's sake than anything...
        prefix = flags.getDatabaseHost() + File.separator + flags.getDatabaseName();
    }

    PlaceholderParser<Block> getBlockParser() throws IOException {
        return getParser(Block.class, BlockPlaceholder::new);
    }

    PlaceholderParser<Course> getCourseParser() throws IOException {
        return getParser(Course.class, CoursePlaceholder::new);
    }

    PlaceholderParser<Room> getRoomParser() throws IOException {
        return getParser(Room.class, RoomPlaceholder::new);
    }

    PlaceholderParser<Professor> getProfessorParser() throws IOException {
        return getParser(Professor.class, ProfessorPlaceholder::new);
    }

    private <T> PlaceholderParser<T> getParser(final Class<T> clazz, final Function<String[], T> constructor) throws IOException {
        System.out.println("Reading placeholder " + clazz.getSimpleName().toLowerCase() + "s");
        return new PlaceholderParser<>(getPath(clazz), constructor);
    }

    private Path getPath(final Class<?> clazz) {
        return new File(prefix + File.separator + clazz.getSimpleName().toLowerCase() + ".csv").toPath();
    }

}
