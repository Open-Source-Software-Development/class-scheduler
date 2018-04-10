package osd.main;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import osd.database.RecordAccession;
import osd.database.output.RunRecord;
import osd.database.output.SeasonRecord;
import osd.schedule.Callbacks;

import java.util.Comparator;

@Module
abstract class MainModule {

    private static RunRecord runRecord;
    private static SeasonRecord seasonRecord;

    @Provides
    static SeasonRecord providesSeasonRecord(final Save save, final RecordAccession recordAccession) {
        if (seasonRecord == null) {
            seasonRecord = recordAccession.getAll(SeasonRecord.class)
                    .sorted(Comparator.comparing(SeasonRecord::getId).reversed())
                    .findFirst()
                    .orElseGet(() -> save.save(new SeasonRecord()));
        }
        return seasonRecord;
    }

    @Provides
    static RunRecord providesRunRecord(final Save save, final SeasonRecord season) {
        if (runRecord == null) {
            runRecord = new RunRecord();
            runRecord.setSeasonId(season.getId());
            save.save(runRecord);
        }
        return runRecord;
    }

    @Binds
    abstract Callbacks bindsCallbacks(SchedulingCallbacks callbacks);

}
