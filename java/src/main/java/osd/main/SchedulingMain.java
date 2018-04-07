package osd.main;

import dagger.Component;
import osd.considerations.ConsiderationModule;
import osd.database.HibernateSessionFactoryModule;
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
            MainModule.class,
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

}
