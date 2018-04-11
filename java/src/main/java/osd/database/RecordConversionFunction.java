package osd.database;

import java.lang.reflect.Constructor;
import java.util.function.Function;

interface RecordConversionFunction<T, R> extends Function<T, R> {

    static <T, R> RecordConversionFunction<T, R> of(
            final Constructor<R> constructor, final RecordAccession accession) {
        assert constructor.isAnnotationPresent(RecordConversion.class);
        assert constructor.getParameterTypes().length == 2;
        assert constructor.getParameterTypes()[1] == RecordAccession.class;
        constructor.setAccessible(true);
        return value -> {
            try {
                return constructor.newInstance(value, accession);
            } catch (final ReflectiveOperationException e) {
                throw new AssertionError("Constructor invocation failed", e);
            }
        };
    }



}
