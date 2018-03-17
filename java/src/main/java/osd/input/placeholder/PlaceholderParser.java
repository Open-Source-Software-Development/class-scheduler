package osd.input.placeholder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PlaceholderParser<T> {

    private final Collection<T> entries;

    PlaceholderParser(final Path path, final Function<String[], T> constructor) throws IOException {
        entries = Files.lines(path)
                .map(s -> s.split(","))
                .map(constructor)
                .collect(Collectors.toList());
    }

    Stream<T> stream() {
        return entries.stream();
    }

}
