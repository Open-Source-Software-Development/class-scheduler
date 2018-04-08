package osd.database;

import dagger.Module;
import dagger.Provides;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import osd.util.classpath.Everything;

import javax.persistence.*;

@Module
public class HibernateSessionFactoryModule {

    private static SessionFactory SESSION_FACTORY;

    @Provides
    static SessionFactory providesSessionFactory(final Everything everything) {
        if (SESSION_FACTORY == null) {
            final Configuration configuration = new Configuration();
            everything.annotatedBy(Entity.class).forEach(configuration::addAnnotatedClass);
            SESSION_FACTORY = configuration.configure().buildSessionFactory();
        }
        return SESSION_FACTORY;
    }

}
