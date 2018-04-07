package osd.main;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import osd.database.output.RunRecord;
import osd.schedule.Callbacks;

@Module
abstract class MainModule {

    @Provides
    static RunRecord providesRunRecord(final SessionFactory sessionFactory) {
        final RunRecord result = new RunRecord();
        Transaction transaction = null;
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.save(result);
        } finally {
            if (transaction != null) {
                transaction.commit();
            }
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    @Binds
    abstract Callbacks bindsCallbacks(SchedulingCallbacks callbacks);

}
