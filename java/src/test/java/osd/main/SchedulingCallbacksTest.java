package osd.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.schedule.Results;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class SchedulingCallbacksTest {

    // This class is still very subject to change, so these tests are
    // intentionally very minimal. The only thing that's actually tested
    // is that done() sees all the complete results that were given to it.

    @Mock private Results mockResults;
    @Mock private CompleteScheduleHandler mockHandler;
    private SchedulingCallbacks instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new SchedulingCallbacks(mockHandler);
    }

    @Test
    void onCompleteResult() {
        // Intentionally not testing the return value, since that's
        // not set in stone yet.
        instance.onCompleteResult(mockResults);
        instance.done(true);
        verify(mockHandler).accept(mockResults);
    }

    @Test
    void onBacktrack() {
        // Only testing that these results don't get given to the handler,
        // since the rest is still subject to change.
        instance.onBacktrack(mockResults);
        instance.done(true);
        verify(mockHandler, never()).accept(mockResults);
    }

}