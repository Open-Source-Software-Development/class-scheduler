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

    <T> void save(final T object) {
        try (final Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(object);
            transaction.commit();
        }
    }

}
