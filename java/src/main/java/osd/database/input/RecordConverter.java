package osd.database.input;

import osd.database.input.record.Record;
import osd.database.input.record.RecordStreamer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class RecordConverter {

    private final RecordStreamer records;
    private final Map<Class<?>, Map<Integer, ?>> cache = new HashMap<>();
    private final GenericForeignKeyResolver genericForeignKeyResolver;

    @Inject
    RecordConverter(final RecordStreamer records, final GenericForeignKeyResolver genericForeignKeyResolver) {
        this.records = records;
        this.genericForeignKeyResolver = genericForeignKeyResolver;
    }

    public <T extends SchedulingElement> T get(final Class<T> clazz, final int id) {
        return get0(clazz).get(id);
    }

    public Object getGeneric(final int classId, final int id) {
        final Class<? extends SchedulingElement> clazz = genericForeignKeyResolver.resolve(classId);
        return get(clazz, id);
    }

    <T extends SchedulingElement> Stream<T> getAll(final Class<T> clazz) {
        return get0(clazz).values().stream();
    }

    <T> Stream<T> getDirect(final Class<T> clazz) {
        return convertAll(clazz);
    }

    @SuppressWarnings("unchecked")
    private <T extends SchedulingElement> Map<Integer, T> get0(final Class<T> clazz) {
        return (Map<Integer, T>)cache.computeIfAbsent(clazz, unused -> mapAll(clazz));
    }

    private <T extends SchedulingElement> Map<Integer, T> mapAll(final Class<T> clazz) {
        return convertAll(clazz).collect(Collectors.toMap(SchedulingElement::getId, Function.identity()));
    }

    private <T> Stream<T> convertAll(final Class<T> clazz) {
        final Map<Class<? extends Record>, Constructor<T>> constructors = getConstructors(clazz);
        return constructors.keySet().stream()
                .flatMap(recordType -> {
                    final Constructor<T> ctor = constructors.get(recordType);
                    return records.stream(recordType)
                            .map(record -> convert(record, ctor));
                });
    }

    private <T> T convert(final Object arg, final Constructor<T> constructor) {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(arg, this);
        } catch (final ReflectiveOperationException e) {
            throw new AssertionError("illegal exception thrown invoking @RecordConversion constructor", e);
        }
    }

    @SuppressWarnings("unchecked") // getDeclaredConstructors returns Constructor<?>[]
    private static <T> Map<Class<? extends Record>, Constructor<T>> getConstructors(final Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(ctor -> ctor.isAnnotationPresent(RecordConversion.class))
                .collect(Collectors.toMap(
                        ctor -> (Class<? extends Record>)ctor.getParameterTypes()[0],
                        ctor -> (Constructor<T>)ctor));
    }

}
