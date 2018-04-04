package osd.database;

abstract class Record<T> {

    abstract T create(final RecordAccession lookup);

    abstract int getId();

}
