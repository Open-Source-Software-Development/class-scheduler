package osd.schedule;

import osd.database.Identified;

import java.util.stream.Stream;

public interface Sources {

    <T extends Identified> Stream<T> get(Class<T> clazz);

}
