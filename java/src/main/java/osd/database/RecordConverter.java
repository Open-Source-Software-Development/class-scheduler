package osd.database;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.stream.Stream;

class RecordConverter {

    @Inject
    RecordConverter() {}

    @SuppressWarnings("unchecked")
    <T extends Identified> Stream<T> convertAll(final RecordAccession accession, final Class<T> clazz) {
        return getConstructors(clazz)
                .flatMap(constructor -> accession.getAll(clazz)
                        .filter(RecordConversionPredicate.of(constructor, accession))
                        .map(RecordConversionFunction.of(constructor, accession)));
    }

    @SuppressWarnings("unchecked") // getDeclaredConstructors returns Constructor<?>[]
    private static <T> Stream<Constructor<T>> getConstructors(final Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(RecordConversion.class))
                .map(constructor -> (Constructor<T>)constructor);
    }

}
