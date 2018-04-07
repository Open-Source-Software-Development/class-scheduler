package osd.schedule;

import dagger.Binds;
import dagger.Module;

/**
 * Dagger module to provide the scheduler instance.
 */
@Module
public interface ScheduleModule {

    @Binds
    Scheduler bindsScheduler(SchedulerImpl schedulerImpl);

}
