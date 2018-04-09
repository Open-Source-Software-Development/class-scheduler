package osd.main;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import osd.database.output.RunRecord;
import osd.schedule.Scheduler;

import static org.mockito.Mockito.verify;

class SchedulingAttemptTest {

    @Mock private SessionFactory mockSessionFactory;
    @Mock private Scheduler mockScheduler;
    @Mock private Save mockSave;
    @Mock private RunRecord mockRunRecord;

    private SchedulingAttempt instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new SchedulingAttempt(mockSessionFactory, mockScheduler, mockSave, mockRunRecord);
    }

    @Test
    void run() {
        instance.run();
        verify(mockScheduler).run();
    }

    @Test
    void close() {
        instance.close();
        final InOrder inOrder = Mockito.inOrder(mockRunRecord, mockSave, mockSessionFactory);
        inOrder.verify(mockSave).save(mockRunRecord);
        inOrder.verify(mockSessionFactory).close();
    }

}