package osd.main;

import org.hibernate.SessionFactory;
import osd.database.output.RunRecord;
import osd.schedule.Scheduler;

import javax.inject.Inject;

class SchedulingAttempt implements Runnable, AutoCloseable {

    private final Scheduler scheduler;
    private final Save save;
    private final RunRecord runRecord;
    private final SessionFactory sessionFactory;

    @Inject
    SchedulingAttempt(final SessionFactory sessionFactory, final Scheduler scheduler, final Save save, final RunRecord runRecord) {
        this.scheduler = scheduler;
        this.save = save;
        this.runRecord = runRecord;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void run() {
        scheduler.run();
    }

    @Override
    public void close() {
        runRecord.setActive(false);
        save.save(runRecord);
        sessionFactory.close();
    }

}
