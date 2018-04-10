package osd.database;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class RecordAccession {

    private final RecordStreamer records;
    private final RecordConverter converter;
    private final Map<Class<?>, Map<Integer, ?>> cache = new HashMap<>();
    private final GenericForeignKeyResolver genericForeignKeyResolver;

    @Inject
    RecordAccession(final RecordStreamer records, final RecordConverter converter,
                    final GenericForeignKeyResolver genericForeignKeyResolver) {
        this.records = records;
        this.converter = converter;
        this.genericForeignKeyResolver = genericForeignKeyResolver;
    }

    public <T extends Identified> T get(final Class<T> clazz, final int id) {
        return getMap(clazz).get(id);
    }

    public Object getGeneric(final int classId, final int id) {
        final Class<? extends Identified> clazz = genericForeignKeyResolver.resolve(classId);
        return get(clazz, id);
    }

    public <T extends Identified> Stream<T> getAll(final Class<T> clazz) {
        return getMap(clazz).values().stream();
    }

    @SuppressWarnings("unchecked")
    private <T extends Identified> Map<Integer, T> getMap(final Class<T> clazz) {
        return (Map<Integer, T>)cache.computeIfAbsent(clazz, unused -> mapAll(clazz));
    }

    private <T extends Identified> Map<Integer, T> mapAll(final Class<T> clazz) {
        return convertAll(clazz).collect(Collectors.toMap(Identified::getId, Function.identity()));
    }

    private <T extends Identified> Stream<T> convertAll(final Class<T> clazz) {
        // If the desired class is already a record, no conversion is necessary.
        if (clazz.isAnnotationPresent(Entity.class)) {
            return records.stream(clazz);
        }
        return converter.convertAll(this, clazz);
    }

}
