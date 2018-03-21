package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.output.Hunk;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SchedulerResultsTest {

    @Mock private Hunk mockHunk;
    @Mock private SchedulerLookups mockSchedulerLookups;
    private final int expectedHunkCount = 7;
    private SchedulerResults instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSchedulerLookups.lookupAllHunks()).then(i -> Stream.of(mockHunk));
        instance = new SchedulerResults(expectedHunkCount, mockSchedulerLookups);
    }

    @Test
    void getHunks() {
        final List<Hunk> expected = Collections.singletonList(mockHunk);
        final List<Hunk> result = instance.getHunks();
        assertEquals(expected, result);
    }

    @Test
    void getExpectedHunkCount() {
        assertEquals(expectedHunkCount, instance.getExpectedHunkCount());
    }

}