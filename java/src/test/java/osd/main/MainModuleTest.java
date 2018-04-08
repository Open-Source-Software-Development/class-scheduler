package osd.main;

import dagger.Binds;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.output.RunRecord;
import osd.database.output.SeasonRecord;
import osd.schedule.Callbacks;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MainModuleTest {

    @Mock private SessionFactory mockSessionFactory;
    @Mock private Session mockSession;
    @Mock private Transaction mockTransaction;
    @Mock private SeasonRecord mockSeasonRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
    }

    @Test
    void providesRunRecord() {
        final RunRecord result = MainModule.providesRunRecord(mockSessionFactory, mockSeasonRecord);
        verify(mockSession).save(result);
        assertNotNull(result);
    }

    @Test
    void providesRunRecord_TransactionCommitted() {
        MainModule.providesRunRecord(mockSessionFactory, mockSeasonRecord);
        verify(mockTransaction).commit();
    }

    @Test
    void providesRunRecord_SessionClosed() {
        MainModule.providesRunRecord(mockSessionFactory, mockSeasonRecord);
        verify(mockSession).close();
    }

    @Test
    void providesRunRecord_SessionClosed_EvenAfterException() {
        when(mockSession.save(any())).thenThrow(new RuntimeException());
        try {
            MainModule.providesRunRecord(mockSessionFactory, mockSeasonRecord);
        } catch (final RuntimeException ignored) {}
        verify(mockSession).close();
    }

    @Test
    void bindsCallbacks() throws NoSuchMethodException {
        final Method bindsCallbacks = MainModule.class.getDeclaredMethod("bindsCallbacks", SchedulingCallbacks.class);
        assertEquals(Callbacks.class, bindsCallbacks.getReturnType());
        assertTrue(bindsCallbacks.isAnnotationPresent(Binds.class));
    }

}