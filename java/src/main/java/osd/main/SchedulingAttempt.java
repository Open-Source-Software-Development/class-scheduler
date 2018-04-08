package osd.main;

import org.hibernate.SessionFactory;
import osd.database.output.RunRecord;
import osd.schedule.Scheduler;

import javax.inject.Inject;

class SchedulingAttempt implements Runnable, AutoCloseable {

    private final Scheduler scheduler;
    private final SessionFactory sessionFactory;
    private final RunRecord runRecord;

    @Inject
    SchedulingAttempt(final Scheduler scheduler, final SessionFactory sessionFactory, final RunRecord runRecord) {
        this.scheduler = scheduler;
        this.sessionFactory = sessionFactory;
        this.runRecord = runRecord;
    }

    @Override
    public void run() {
        scheduler.run();
    }

    @Override
    public void close() {
        runRecord.setActive(false);
        Save.save(sessionFactory, runRecord);
    }

}
