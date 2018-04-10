package osd.database.input;

import osd.database.Identified;
import osd.database.RecordAccession;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.stream.Stream;

@Singleton
public class Sources {

    private final RecordAccession recordAccession;

    @Inject
    Sources(final RecordAccession recordAccession) {
        this.recordAccession = recordAccession;
    }

    public <T extends Identified> Stream<T> get(final Class<T> clazz) {
        return recordAccession.getAll(clazz);
    }

    public Stream<Section> getSections() {
        return get(Course.class).flatMap(Course::streamSections);
    }

}
