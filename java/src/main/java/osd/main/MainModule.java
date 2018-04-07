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
interface MainModule {

    @Provides
    static RunRecord providesRunRecord(final SessionFactory sessionFactory) {
        final RunRecord result = new RunRecord();
        Session session = sessionFactory.openSession();
        try {
            final Transaction transaction = session.beginTransaction();
            session.save(result);
            transaction.commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    @Binds
    Callbacks bindsCallbacks(SchedulingCallbacks callbacks);

}
