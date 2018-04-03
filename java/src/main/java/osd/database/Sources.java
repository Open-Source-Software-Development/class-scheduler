package osd.database;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Sources {

    private final From from;
    private final Map<Class<?>, Map<Integer, ?>> CACHE = new HashMap<>();
    private final Map<String, Object> NAMED = new HashMap<>();

    private final RecordLookup RECORD_LOOKUP = new RecordLookup() {

        @Override
        public <T> T get(final Class<T> clazz, final int name) {
            return clazz.cast(lazy(clazz).get(name));
        }

        @Override
        public <T> Stream<T> getAll(Class<T> clazz) {
            return lazy(clazz).values().stream();
        }

    };

    @Inject
    Sources(final From from) {
        this.from = from;
    }

    public Stream<Section> getSections() {
        return lazy(Course.class).values().stream()
                .map(Course::getSections)
                .map(Iterable::spliterator)
                .flatMap(spliterator -> StreamSupport.stream(spliterator, false));
    }

    public Stream<Professor> getProfessors() {
        return lazy(Professor.class).values().stream();
    }

    public Stream<Block> getBlocks() {
        return lazy(Block.class).values().stream();
    }

    public Stream<Room> getRooms() {
        return lazy(Room.class).values().stream();
    }

    @SuppressWarnings("unchecked")
    private <T> Map<Integer, T> lazy(final Class<T> clazz) {
        return (Map<Integer, T>)CACHE.computeIfAbsent(clazz, this::initCacheEntry);
    }

    private <T> Map<Integer, T> initCacheEntry(final Class<T> clazz) {
        final Map<Integer, T> result = new HashMap<>();
        for (final Record<T> record : from.from(clazz)) {
            final T t = record.create(RECORD_LOOKUP);
            result.put(record.getId(), t);
            NAMED.put(record.getName(), t);
        }
        return result;
    }

}
