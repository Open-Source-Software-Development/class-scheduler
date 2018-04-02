package osd.database.placeholder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

class PlaceholderParser<T> {

    private final Map<String, T> entries = new HashMap<>();

    PlaceholderParser(final Path path, final Function<String[], T> constructor) throws IOException {
        Files.lines(path)
                .map(s -> s.split(","))
                .map(constructor)
                .forEach(placeholder -> entries.put(placeholder.toString(), placeholder));
    }

    Stream<T> stream() {
        return entries.values().stream();
    }

    T lookup(final String name) {
        return entries.get(name);
    }

}
