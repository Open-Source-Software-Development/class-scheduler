package osd.database.placeholder;

import osd.database.*;
import osd.main.Flags;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Singleton
class PlaceholderConfig {

    private final String prefix;
    private final PlaceholderParser<Block> blockParser;
    private final PlaceholderParser<Course> courseParser;
    private final PlaceholderParser<Professor> professorParser;
    private final PlaceholderParser<Room> roomParser;
    private final PlaceholderParser<UserConstraintRecord> userConstraintParser;

    @Inject
    PlaceholderConfig(final Flags flags) {
        // Using these flags more for example's sake than anything...
        prefix = flags.getDatabaseHost() + File.separator + flags.getDatabaseName();
        try {
            this.blockParser = getParser(Block.class, BlockPlaceholder::new);
            this.courseParser = getParser(Course.class, CoursePlaceholder::new);
            this.roomParser = getParser(Room.class, RoomPlaceholder::new);
            this.professorParser = getParser(Professor.class, ProfessorPlaceholder::new);
            this.userConstraintParser = getParser(UserConstraintRecord.class,
                    row -> new UserConstraintPlaceholder(row, this));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    Supplier<Stream<Block>> getBlockParser() {
        return blockParser::stream;
    }

    Supplier<Stream<Course>> getCourseParser() {
        return courseParser::stream;
    }

    Supplier<Stream<Room>> getRoomParser() {
        return roomParser::stream;
    }

    Supplier<Stream<Professor>> getProfessorParser() {
        return professorParser::stream;
    }

    Stream<UserConstraintRecord> getUserConstraints() {
        return userConstraintParser.stream();
    }

    Object lookup(final String key) {
        return Stream.of(blockParser, courseParser, roomParser, professorParser)
                .map(parser -> parser.lookup(key))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(key));
    }

    private <T> PlaceholderParser<T> getParser(final Class<T> clazz,
                                               final Function<String[], T> constructor) throws IOException {
        return new PlaceholderParser<>(getPath(clazz), constructor);
    }

    private Path getPath(final Class<?> clazz) {
        final String className = clazz.getSimpleName().toLowerCase();
        final String fileName = className.replace("record", "");
        return new File(prefix + File.separator + fileName + ".csv").toPath();
    }

}
