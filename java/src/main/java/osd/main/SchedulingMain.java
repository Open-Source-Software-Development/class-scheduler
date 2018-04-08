package osd.main;

import dagger.Component;
import osd.considerations.ConsiderationModule;
import osd.database.HibernateSessionFactoryModule;
import osd.schedule.ScheduleModule;
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

    abstract SchedulingAttempt schedulingAttempt();

    public static void main(final String[] args) {
        Exception exception;
        final SchedulingMain main = DaggerSchedulingMain.create();
        try (final SchedulingAttempt attempt = main.schedulingAttempt()){
            attempt.run();
            exception = null;
        } catch (final RuntimeException e) {
            e.printStackTrace();
            exception = e;
        }
        System.exit(exception == null ? 0 : 1);
    }

}
