package osd.main;

import dagger.Binds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.input.record.RecordStreamer;
import osd.database.output.RunRecord;
import osd.database.output.SeasonRecord;
import osd.schedule.Callbacks;

import java.lang.reflect.Method;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainModuleTest {

    @SuppressWarnings("unused")
    private static final MainModule FIX_COVERAGE_REPORT = new MainModule() {
        @Override
        Callbacks bindsCallbacks(SchedulingCallbacks callbacks) {
            return null;
        }
    };

    @Mock private Save mockSave;
    @Mock private SeasonRecord mockSeasonRecord;
    @Mock private RecordStreamer mockRecordStreamer;

    private final int seasonId = (int)(Math.random() * 1000) + 500;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSave.save(any())).then(invocation -> invocation.getArgument(0));
        when(mockSeasonRecord.getId()).thenReturn(seasonId);
        when(mockRecordStreamer.stream(SeasonRecord.class)).then(unused ->
            Stream.concat(IntStream.range(0, 20)
                    .mapToObj(i -> {
                        final SeasonRecord mockRecord = mock(SeasonRecord.class);
                        when(mockRecord.getId()).thenReturn(i);
                        return mockRecord;
                    }), Stream.of(mockSeasonRecord)));
    }

    @Test
    void providesSeasonRecord() {
        final SeasonRecord result = MainModule.providesSeasonRecord(mockSave, mockRecordStreamer);
        assertEquals(result, mockSeasonRecord);
    }

    @Test
    void providesSeasonRecord_Cache() {
        final SeasonRecord result1 = MainModule.providesSeasonRecord(mockSave, mockRecordStreamer);
        final SeasonRecord result2 = MainModule.providesSeasonRecord(mockSave, mockRecordStreamer);
        assertTrue(result1 == result2);
    }

    @Test
    void providesRunRecord() {
        final RunRecord result = MainModule.providesRunRecord(mockSave, mockSeasonRecord);
        verify(mockSave).save(result);
        assertNotNull(result);
        assertEquals(mockSeasonRecord.getId(), result.getSeasonId());
    }

    @Test
    void providesRunRecord_Cache() {
        final RunRecord result1 = MainModule.providesRunRecord(mockSave, mockSeasonRecord);
        final RunRecord result2 = MainModule.providesRunRecord(mockSave, mockSeasonRecord);
        assertTrue(result1 == result2);
    }

    @Test
    void bindsCallbacks() throws NoSuchMethodException {
        final Method bindsCallbacks = MainModule.class.getDeclaredMethod("bindsCallbacks", SchedulingCallbacks.class);
        assertEquals(Callbacks.class, bindsCallbacks.getReturnType());
        assertTrue(bindsCallbacks.isAnnotationPresent(Binds.class));
    }

}