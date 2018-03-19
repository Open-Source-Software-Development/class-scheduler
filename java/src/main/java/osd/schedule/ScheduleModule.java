package osd.schedule;

import dagger.Binds;
import dagger.Module;
import osd.considerations.ConsiderationModule;

@Module(
        includes = ConsiderationModule.class
)
public interface ScheduleModule {

    @Binds
    Scheduler providesScheduler(SchedulerImpl impl);
}
