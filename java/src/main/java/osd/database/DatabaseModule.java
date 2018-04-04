package osd.database;

import dagger.Module;
import dagger.Provides;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import osd.considerations.UserConstraint;
import osd.considerations.UserPreference;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.*;

@Module
public class DatabaseModule {

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

    @Provides
    static Collection<UserConstraint> providesUserConstraints(final RecordAccession recordAccession) {
        return Stream.concat(
                recordAccession.getAllFromDefaultRecord(UserConstraint.class),
                recordAccession.getAll(UserConstraint.class, QualificationRecord.class)
        ).collect(Collectors.toList());
    }

    @Provides
    static Collection<UserPreference> providesUserPreferences(final RecordAccession recordAccession) {
        return recordAccession.getAllFromDefaultRecord(UserPreference.class).collect(Collectors.toList());
    }

}
