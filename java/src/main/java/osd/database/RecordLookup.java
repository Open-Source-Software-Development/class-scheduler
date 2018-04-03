package osd.database;

import java.util.stream.Stream;

interface RecordLookup {

    <T> T get(final Class<T> clazz, final int id);

    <T> Stream<T> getAll(final Class<T> clazz);

}
