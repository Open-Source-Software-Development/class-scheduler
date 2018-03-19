package osd.schedule;

import dagger.Binds;
import dagger.Module;
import osd.considerations.BaseConsiderationModule;

@Module(
        includes = BaseConsiderationModule.class
)
public interface ScheduleModule {

    @Binds
    Scheduler providesScheduler(SchedulerImpl impl);
}
