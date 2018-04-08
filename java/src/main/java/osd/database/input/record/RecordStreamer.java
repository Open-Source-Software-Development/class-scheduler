package osd.database.input.record;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class RecordStreamer {

    private final SessionFactory sessionFactory;

    private final Map<Class<?>, List<?>> records = new HashMap<>();

    @Inject
    RecordStreamer(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> Stream<T> stream(final Class<T> recordType) {
        return (Stream<T>)(records.computeIfAbsent(recordType, this::readRecords)).stream();
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> readRecords(final Class<T> recordType) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            final Query query = session.createQuery( "FROM " + recordType.getSimpleName());
            return (List<T>)query.list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
