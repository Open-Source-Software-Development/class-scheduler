package osd.main;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import osd.database.RecordAccession;
import osd.database.output.RunRecord;
import osd.database.output.SeasonRecord;
import osd.schedule.Callbacks;
import osd.schedule.Sources;

import javax.inject.Singleton;
import java.util.Comparator;

@Module
interface MainModule {

    @Provides
    @Singleton
    static SeasonRecord providesSeasonRecord(final RecordAccession recordAccession) {
        return recordAccession.getAll(SeasonRecord.class)
                .sorted(Comparator.comparing(SeasonRecord::getId).reversed())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No seasons present in database"));
    }

    @Provides
    @Singleton
    static RunRecord providesRunRecord(final Save save, final SeasonRecord season) {
        final RunRecord runRecord = new RunRecord();
        runRecord.setSeasonId(season.getId());
        save.save(runRecord);
        return runRecord;
    }

    @Binds
    Callbacks bindsCallbacks(CallbacksImpl callbacks);

    @Binds
    Sources bindsSources(SourcesImpl sources);

}
