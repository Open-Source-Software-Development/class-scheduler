package osd.database.input;

import osd.database.input.record.RecordStreamer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
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
        return getConstructors(clazz)
                .flatMap(constructor -> {
                    final Class<?> recordType = constructor.getParameterTypes()[0];
                    return records.stream(recordType)
                            .filter(getFilter(constructor))
                            .map(record -> convert(record, constructor));
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
    private static <T> Stream<Constructor<T>> getConstructors(final Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(RecordConversion.class))
                .map(constructor -> (Constructor<T>)constructor);
    }

    private static Predicate<Object> getFilter(final Constructor<?> constructor) {
        final String filterName = constructor.getAnnotation(RecordConversion.class).filter();
        if (filterName.equals("")) {
            return anything -> true;
        }
        final Class<?> recordType = constructor.getParameterTypes()[0];
        try {
            final Method filter = constructor.getDeclaringClass().getDeclaredMethod(filterName, recordType);
            filter.setAccessible(true);
            return record -> {
                try {
                    return (Boolean)filter.invoke(null, record);
                } catch (final ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (final NoSuchMethodException e) {
            throw new AssertionError(constructor + " has invalid @RecordConversion: no such filter() method");
        }
    }

}
