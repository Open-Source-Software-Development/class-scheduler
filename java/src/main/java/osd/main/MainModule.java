package osd.main;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import osd.database.input.record.RecordStreamer;
import osd.database.output.RunRecord;
import osd.database.output.SeasonRecord;
import osd.schedule.Callbacks;

import java.util.Comparator;

@Module
abstract class MainModule {

    private static RunRecord runRecord;
    private static SeasonRecord seasonRecord;

    @Provides
    static SeasonRecord providesSeasonRecord(final Save save, final RecordStreamer recordStreamer) {
        if (seasonRecord == null) {
            seasonRecord = recordStreamer.stream(SeasonRecord.class)
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
            runRecord.setActive(true);
            runRecord.setPid(pid());
            save.save(runRecord);
        }
        return runRecord;
    }

    @Binds
    abstract Callbacks bindsCallbacks(SchedulingCallbacks callbacks);

    private static int pid() {
        // Taken from https://stackoverflow.com/a/12066696/8560257
        try {
            final java.lang.management.RuntimeMXBean runtime =
                    java.lang.management.ManagementFactory.getRuntimeMXBean();
            final java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            final sun.management.VMManagement management =
                    (sun.management.VMManagement) jvm.get(runtime);
            final java.lang.reflect.Method pid_method =
                    management.getClass().getDeclaredMethod("getProcessId");
            pid_method.setAccessible(true);
            return (Integer)pid_method.invoke(management);
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
