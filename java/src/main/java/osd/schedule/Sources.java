package osd.schedule;

import osd.database.Identified;

import java.util.stream.Stream;

/**
 * Data sources for the scheduler. An implementation must be made available
 * (via dependency injection) to the algorithm so it can find its data.
 */
public interface Sources {

    <T extends Identified> Stream<T> get(Class<T> clazz);

}
