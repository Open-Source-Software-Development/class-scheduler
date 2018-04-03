package osd.database;

abstract class Record<T> extends AbstractSchedulingElement {

    abstract T create(final RecordLookup lookup);

    abstract int getId();

}
