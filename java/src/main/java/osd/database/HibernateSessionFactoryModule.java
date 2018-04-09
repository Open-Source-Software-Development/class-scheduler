package osd.database;

import dagger.Module;
import dagger.Provides;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import osd.util.classpath.Everything;

import javax.inject.Singleton;
import javax.persistence.*;

@Module
public interface HibernateSessionFactoryModule {

    @Provides
    @Singleton
    static SessionFactory providesSessionFactory(final Everything everything) {
            final Configuration configuration = new Configuration();
            everything.annotatedBy(Entity.class).forEach(configuration::addAnnotatedClass);
            return configuration.configure().buildSessionFactory();
    }

}
