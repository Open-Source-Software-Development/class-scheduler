package osd.database;

abstract class Record<T> extends AbstractSchedulingElement {

    abstract T create(final RecordAccession lookup);

    abstract int getId();

}
