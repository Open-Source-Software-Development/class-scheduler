package osd.main;

import osd.database.Identified;
import osd.database.RecordAccession;
import osd.database.input.Course;
import osd.database.input.Section;
import osd.database.input.record.SeasonCourseRecord;
import osd.database.output.SeasonRecord;
import osd.schedule.Sources;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
class SourcesImpl implements Sources {

    private final RecordAccession recordAccession;
    private final Set<Integer> includeCourseIds;

    @Inject
    SourcesImpl(final SeasonRecord season, final RecordAccession recordAccession) {
        this.recordAccession = recordAccession;
        includeCourseIds = recordAccession.getAll(SeasonCourseRecord.class)
                .filter(record -> record.getSeasonId() == season.getId())
                .map(SeasonCourseRecord::getCourseId)
                .collect(Collectors.toSet());
    }

    @Override
    public <T extends Identified> Stream<T> get(final Class<T> clazz) {
        return recordAccession.getAll(clazz)
                .filter(this::filter);
    }

    private boolean filter(final Object o) {
        if (o instanceof Course) {
            final int courseId = ((Course)o).getId();
            return includeCourseIds.contains(courseId);
        }
        if (o instanceof Section) {
            final Course course = ((Section)o).getCourse();
            return filter(course);
        }
        return true;
    }

}
