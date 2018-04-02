package osd.database;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

class From {

    private final SessionFactory sessionFactory;

    From(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    <T> List<T> from(final Class<T> interfaceType) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            final Query query = session.createQuery( "FROM " + interfaceType.getSimpleName());
            return validateQueryResult(query, interfaceType);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> validateQueryResult(final Query query, final Class<T> interfaceType) {
        final List<?> list = query.list();
        list.forEach(interfaceType::cast);
        return (List<T>)list;
    }

}
