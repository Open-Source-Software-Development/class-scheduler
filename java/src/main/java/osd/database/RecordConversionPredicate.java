package osd.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

interface RecordConversionPredicate extends Predicate<Object> {

    @SuppressWarnings("unchecked")
    static RecordConversionPredicate of(final Constructor<?> constructor, final RecordAccession accession) {
        final RecordConversion data = constructor.getAnnotation(RecordConversion.class);
        assert data != null;
        final Class<? extends Predicate> filterClass = data.filter();
        try {
            final Constructor<? extends Predicate> filterConstructor =
                    filterClass.getDeclaredConstructor(RecordAccession.class);
            filterConstructor.setAccessible(true);
            final Predicate filter = filterConstructor.newInstance(accession);
            return filter::test;
        } catch (final NoSuchMethodException e) {
            throw new AssertionError(filterClass + " does not have a constructor taking a single RecordAccession");
        } catch (final InstantiationException | InvocationTargetException e) {
            throw new AssertionError("Error constructing " + filterClass, e);
        } catch (final IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    class Dummy implements RecordConversionPredicate {

        @SuppressWarnings("unused")
        Dummy(final RecordAccession unused) {}

        @Override
        public boolean test(final Object o) {
            return true;
        }
    }

}
