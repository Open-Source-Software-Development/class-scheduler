package osd.database;

import dagger.Module;
import dagger.Provides;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import osd.considerations.UserConstraint;
import osd.considerations.UserPreference;

import java.util.Collection;
import java.util.Collections;

@Module
public class DatabaseModule {

    @Provides
    static SessionFactory providesSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }

    @Provides
    static Collection<UserConstraint> providesUserConstraints() {
        return Collections.emptyList();
    }

    @Provides
    static Collection<UserPreference> providesUserPreferences() {
        return Collections.emptyList();
    }

}
