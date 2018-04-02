package osd.schedule;

import dagger.Binds;
import dagger.Module;

/**
 * Dagger module to provide the scheduler instance.
 */
@Module
public abstract class ScheduleModule {

    @Binds
    abstract Scheduler bindsScheduler(SchedulerImpl schedulerImpl);

}
