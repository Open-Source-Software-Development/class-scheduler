package osd.main;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import osd.considerations.BaseConsiderationModule;
import osd.considerations.ConsiderationModule;
import osd.database.DatabaseModule;
import osd.schedule.Callbacks;
import osd.schedule.ScheduleModule;
import osd.schedule.Scheduler;

import javax.inject.Singleton;

@Singleton
@Component(
    modules={
            DatabaseModule.class,
            ConsiderationModule.class,
            ScheduleModule.class,
            Scheduling.DemoCallbacksModule.class,
    }
)
public abstract class Scheduling {

    abstract Scheduler schedulingAttempt();

    public static void main(final String[] args) {
        // Demo code.
        Scheduling scheduling = DaggerScheduling.builder()
                .baseConsiderationModule(new BaseConsiderationModule(FastClasspathScanner::new))
                .build();

        scheduling.schedulingAttempt().run();
    }

    @Module
    abstract class DemoCallbacksModule {

        @Binds
        abstract Callbacks bindsCallbacks(DemoCallbacks demoCallbacks);

    }

}
