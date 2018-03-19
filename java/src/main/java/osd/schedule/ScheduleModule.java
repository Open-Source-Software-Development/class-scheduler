package osd.schedule;

import dagger.Binds;
import dagger.Module;
import osd.considerations.BaseConsiderationModule;

/**
 * Dagger module to provide the scheduler instance.
 */
@Module(
        includes = BaseConsiderationModule.class
)
public interface ScheduleModule {

    @Binds
    Scheduler providesScheduler(SchedulerImpl impl);
}
