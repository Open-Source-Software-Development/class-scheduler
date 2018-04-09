package osd.database.input;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.stream.Stream;

@Singleton
public class Sources {

    private final RecordConverter recordConverter;

    @Inject
    Sources(final RecordConverter recordConverter) {
        this.recordConverter = recordConverter;
    }

    public <T> Stream<T> getDirect(final Class<T> clazz) {
        return recordConverter.getDirect(clazz);
    }

    public <T extends SchedulingElement> Stream<T> get(final Class<T> clazz) {
        return recordConverter.getAll(clazz);
    }

    public Stream<Section> getSections() {
        return get(Course.class).flatMap(Course::streamSections);
    }

}
