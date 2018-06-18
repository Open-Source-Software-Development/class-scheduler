package osd.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SaveTest {

    @Mock private SessionFactory mockSessionFactory;
    @Mock private Session mockSession;
    @Mock private Transaction mockTransaction;

    private final Object saveThis = new Object();
    private Save instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSessionFactory.openSession()).thenReturn(mockSession);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        instance = new Save(mockSessionFactory);
    }

    @Test
    void save() {
        instance.save(saveThis);
        final InOrder inOrder = inOrder(mockSessionFactory, mockSession, mockTransaction);
        inOrder.verify(mockSessionFactory).openSession();
        inOrder.verify(mockSession).beginTransaction();
        inOrder.verify(mockSession).saveOrUpdate(saveThis);
        inOrder.verify(mockTransaction).commit();
        inOrder.verify(mockSession).close();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void save_Close_EvenOnException() {
        final String message = "testing resource leak wrt exceptions";
        doThrow(new RuntimeException(message)).when(mockSession).saveOrUpdate(any());
        try {
            instance.save(saveThis);
        } catch (final RuntimeException e) {
            if (!message.equals(e.getMessage())) {
                throw e;
            }
        }
        verify(mockSession).close();
    }

}