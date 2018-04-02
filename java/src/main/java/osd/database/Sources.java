package osd.database;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Sources {

    private final From from;
    private final Map<Class<?>, List<?>> CACHE = new HashMap<>();

    @Inject
    Sources(final From from) {
        this.from = from;
    }

    public Stream<Section> getSections() {
        // TODO: replace this with a getCourses
        return lazy(Course.class).stream()
                .map(Course::getSections)
                .map(Iterable::spliterator)
                .flatMap(spliterator -> StreamSupport.stream(spliterator, false));
    }

    public Stream<Professor> getProfessors() {
        return lazy(Professor.class).stream();
    }

    public Stream<Block> getBlocks() {
        return lazy(Block.class).stream();
    }

    public Stream<Room> getRooms() {
        return lazy(Room.class).stream();
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> lazy(final Class<T> clazz) {
        return (List<T>)CACHE.computeIfAbsent(clazz, from::from);
    }

}
