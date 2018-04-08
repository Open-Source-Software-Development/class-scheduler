package osd.main;

import dagger.Binds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.output.RunRecord;
import osd.database.output.SeasonRecord;
import osd.schedule.Callbacks;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainModuleTest {

    @Mock private Save mockSave;
    @Mock private SeasonRecord mockSeasonRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSave.save(any())).then(invocation -> invocation.getArgument(0));
        when(mockSeasonRecord.getId()).thenReturn((int)(Math.random() * 1000));
    }

    @Test
    void providesRunRecord() {
        final RunRecord result = MainModule.providesRunRecord(mockSave, mockSeasonRecord);
        verify(mockSave).save(result);
        assertNotNull(result);
        assertTrue(result.getActive());
        assertEquals(mockSeasonRecord.getId(), result.getSeasonId());
        assertNotEquals(0, result.getPid());
    }

    @Test
    void bindsCallbacks() throws NoSuchMethodException {
        final Method bindsCallbacks = MainModule.class.getDeclaredMethod("bindsCallbacks", SchedulingCallbacks.class);
        assertEquals(Callbacks.class, bindsCallbacks.getReturnType());
        assertTrue(bindsCallbacks.isAnnotationPresent(Binds.class));
    }

}