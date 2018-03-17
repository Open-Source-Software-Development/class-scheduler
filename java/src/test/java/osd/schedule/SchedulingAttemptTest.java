package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.input.Section;
import osd.output.Hunk;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SchedulingAttemptTest {

    @Mock private Hunk mockHunk;
    @Mock private Scheduler mockScheduler;
    @Mock private Section mockSection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockScheduler.addHunk(mockHunk)).thenReturn(true);
        when(mockScheduler.copy()).thenReturn(mockScheduler);
    }

    @Test
    void call() {
        when(mockScheduler.getCandidateHunks(any())).thenReturn(Collections.singleton(mockHunk));
        when(mockScheduler.getNextSection()).thenReturn(mockSection, null, null);
        final SchedulingAttempt instance = new SchedulingAttempt(mockScheduler);
        final List<Hunk> expected = Collections.singletonList(mockHunk);
        final List<Hunk> result = instance.call();
        assertEquals(expected, result);
        verify(mockScheduler).addHunk(mockHunk);
    }

}