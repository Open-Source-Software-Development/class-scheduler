package osd.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.TypedQuery;
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
            final TypedQuery<T> query = session.createQuery( "FROM " + recordType.getSimpleName(), recordType);
            return query.getResultList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
