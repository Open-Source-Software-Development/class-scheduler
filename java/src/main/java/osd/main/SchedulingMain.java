package osd.main;

import dagger.Component;
import osd.considerations.ConsiderationModule;
import osd.database.HibernateSessionFactoryModule;
import osd.util.classpath.ClasspathModule;

import javax.inject.Singleton;

@Singleton
@Component(
    modules={
            HibernateSessionFactoryModule.class,
            ConsiderationModule.class,
            MainModule.class,
            ClasspathModule.class,
    }
)
interface SchedulingMain {

    SchedulingAttempt schedulingAttempt();

    static void main(final String[] args) {
        final SchedulingMain main = DaggerSchedulingMain.create();
        try (final SchedulingAttempt attempt = main.schedulingAttempt()){
            attempt.run();
        }
    }

}
