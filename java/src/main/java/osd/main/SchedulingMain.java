package osd.main;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import osd.considerations.ConsiderationModule;
import osd.database.HibernateSessionFactoryModule;
import osd.schedule.Callbacks;
import osd.schedule.ScheduleModule;
import osd.schedule.Scheduler;
import osd.util.classpath.ClasspathModule;

import javax.inject.Singleton;

@Singleton
@Component(
    modules={
            HibernateSessionFactoryModule.class,
            ConsiderationModule.class,
            ScheduleModule.class,
            SchedulingMain.MainModule.class,
            ClasspathModule.class,
    }
)
public abstract class SchedulingMain {

    abstract Scheduler schedulingAttempt();

    public static void main(final String[] args) {
        DaggerSchedulingMain.create()
                .schedulingAttempt()
                .run();
        System.exit(0);
    }

    @Module
    abstract class MainModule {

        @Binds
        abstract Callbacks bindsCallbacks(SchedulingCallbacks callbacks);

    }

}
