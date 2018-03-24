package osd.schedule;

import dagger.Binds;
import dagger.Module;
import osd.considerations.BaseConsiderationModule;
import osd.considerations.UserConsiderationModule;
import osd.database.placeholder.PlaceholderModule;

/**
 * Dagger module to provide the scheduler instance.
 */
@Module(
        includes = {
                // TODO: once placeholders aren't needed, replace this with DatabaseModule
                PlaceholderModule.class,
                UserConsiderationModule.class,
                BaseConsiderationModule.class,
        }
)
public interface ScheduleModule {

    @Binds
    Scheduler providesScheduler(SchedulerImpl impl);
}
