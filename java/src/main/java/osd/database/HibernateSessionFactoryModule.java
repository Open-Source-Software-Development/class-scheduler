package osd.database;

import dagger.Module;
import dagger.Provides;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.*;

@Module
public class HibernateSessionFactoryModule {

    private static final SessionFactory SESSION_FACTORY;
    static {
        final Configuration configuration = new Configuration();
        new FastClasspathScanner()
                .matchClassesWithAnnotation(Entity.class, configuration::addAnnotatedClass)
                .scan();
        SESSION_FACTORY = configuration.configure().buildSessionFactory();
    }

    @Provides
    static SessionFactory providesSessionFactory() {
        return SESSION_FACTORY;
    }

}
