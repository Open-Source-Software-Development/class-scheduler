package osd.main;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.commons.cli.ParseException;
import osd.considerations.BaseConsiderationModule;
import osd.considerations.ConsiderationModule;
import osd.database.placeholder.PlaceholderModule;
import osd.flags.FlagModule;
import osd.schedule.Callbacks;
import osd.schedule.ScheduleModule;
import osd.schedule.Scheduler;

import javax.inject.Singleton;

@Singleton
@Component(
    modules={
            PlaceholderModule.class, // TODO: replace this with osd.database.DatabaseModule
            ConsiderationModule.class,
            ScheduleModule.class,
            Scheduling.DemoCallbacksModule.class,
    }
)
public abstract class Scheduling {

    abstract Scheduler schedulingAttempt();

    public static void main(final String[] args) throws ParseException {
        // Demo code.
        Scheduling scheduling = DaggerScheduling.builder()
                .flagModule(new FlagModule(args))
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
