package osd.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

class Save {

    static <T> T save(final SessionFactory sessionFactory, final T object) {
        Session session = sessionFactory.openSession();
        try {
            final Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(object);
            transaction.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return object;
    }

}
