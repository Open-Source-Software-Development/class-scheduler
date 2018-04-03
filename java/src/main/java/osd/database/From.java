package osd.database;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.List;

class From {

    private final SessionFactory sessionFactory;

    @Inject
    From(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    <T> List<Record<T>> from(final Class<T> interfaceType) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            final Query query = session.createQuery( "FROM " + interfaceType.getSimpleName() + "Record");
            return validateQueryResult(query, interfaceType);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<Record<T>> validateQueryResult(final Query query, final Class<T> interfaceType) {
        return (List<Record<T>>)query.list();
    }

}
