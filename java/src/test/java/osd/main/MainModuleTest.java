package osd.main;

import dagger.Binds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.RecordAccession;
import osd.database.output.RunRecord;
import osd.database.output.SeasonRecord;
import osd.schedule.Callbacks;

import java.lang.reflect.Method;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainModuleTest {

    @Mock private Save mockSave;
    @Mock private SeasonRecord mockSeasonRecord;
    @Mock private RecordAccession mockRecordAccession;

    private final int seasonId = (int)(Math.random() * 1000) + 500;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSeasonRecord.getId()).thenReturn(seasonId);
        when(mockRecordAccession.getAll(SeasonRecord.class)).then(unused ->
            Stream.concat(IntStream.range(0, 20)
                    .mapToObj(i -> {
                        final SeasonRecord mockRecord = mock(SeasonRecord.class);
                        when(mockRecord.getId()).thenReturn(i);
                        return mockRecord;
                    }), Stream.of(mockSeasonRecord)));
    }

    @Test
    void providesSeasonRecord() {
        final SeasonRecord result = MainModule.providesSeasonRecord(mockRecordAccession);
        assertEquals(result, mockSeasonRecord);
    }

    @Test
    void providesSeasonRecord_ExceptionOnNone() {
        when(mockRecordAccession.getAll(SeasonRecord.class)).thenReturn(Stream.empty());
        assertThrows(IllegalStateException.class,
                () -> MainModule.providesSeasonRecord(mockRecordAccession));
    }

    @Test
    void providesRunRecord() {
        final RunRecord result = MainModule.providesRunRecord(mockSave, mockSeasonRecord);
        verify(mockSave).save(result);
        assertNotNull(result);
        assertEquals(mockSeasonRecord.getId(), result.getSeasonId());
    }

    @Test
    void bindsCallbacks() throws NoSuchMethodException {
        final Method bindsCallbacks = MainModule.class.getDeclaredMethod("bindsCallbacks", CallbacksImpl.class);
        assertEquals(Callbacks.class, bindsCallbacks.getReturnType());
        assertTrue(bindsCallbacks.isAnnotationPresent(Binds.class));
    }

}