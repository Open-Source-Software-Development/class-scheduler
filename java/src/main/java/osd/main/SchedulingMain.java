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
            SchedulingMain.MainModule.class,
    }
)
public abstract class SchedulingMain {

    abstract Scheduler schedulingAttempt();

    public static void main(final String[] args) {
        DaggerSchedulingMain.builder()
                .baseConsiderationModule(new BaseConsiderationModule(FastClasspathScanner::new))
                .build()
                .schedulingAttempt()
                .run();
        System.exit(0);
    }

    @Module
    abstract class MainModule {

        @Binds
        abstract Callbacks bindsCallbacks(SchedulingCallbacks demoCallbacks);

    }

}
