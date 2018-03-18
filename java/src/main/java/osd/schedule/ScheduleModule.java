package osd.schedule;

import dagger.Binds;
import dagger.Module;

@Module
public interface ScheduleModule {

    @Binds
    Scheduler providesScheduler(SchedulerImpl impl);
}
