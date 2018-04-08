package osd.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.inject.Inject;

class Save {

    private final SessionFactory sessionFactory;

    @Inject
    Save(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    <T> T save(final T object) {
        final Session session = sessionFactory.openSession();
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
