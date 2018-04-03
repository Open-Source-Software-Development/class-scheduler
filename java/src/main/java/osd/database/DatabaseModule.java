package osd.database;

import dagger.Module;
import dagger.Provides;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import osd.considerations.UserConstraint;
import osd.considerations.UserPreference;

import java.util.Collection;
import java.util.stream.Collectors;

@Module
public class DatabaseModule {

    private static final SessionFactory SESSION_FACTORY = new Configuration().configure().buildSessionFactory();

    @Provides
    static SessionFactory providesSessionFactory() {
        return SESSION_FACTORY;
    }

    @Provides
    static Collection<UserConstraint> providesUserConstraints(final RecordAccession recordAccession) {
        return recordAccession.getAll(UserConstraint.class).collect(Collectors.toList());
    }

    @Provides
    static Collection<UserPreference> providesUserPreferences(final RecordAccession recordAccession) {
        return recordAccession.getAll(UserPreference.class).collect(Collectors.toList());
    }

}
