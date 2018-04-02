package osd.database;

import dagger.Module;
import dagger.Provides;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Module
public class DatabaseModule {

    private static final Class<?>[] RECORD_TYPES = {
            BlockRecord.class, RoomRecord.class, ProfessorRecord.class,
            CourseRecord.class, UserConstraintRecord.class, UserPreferenceRecord.class
    };

    @Provides
    static SessionFactory providesSessionFactory() {
        final Configuration configuration = new Configuration().configure();
        for (final Class<?> recordType: RECORD_TYPES) {
            configuration.addAnnotatedClass(recordType);
        }
        return configuration.buildSessionFactory();
    }

}
