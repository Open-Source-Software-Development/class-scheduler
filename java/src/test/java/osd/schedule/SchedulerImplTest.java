package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SchedulerImplTest {

    /* HOW THIS TEST WORKS:
    We have four mock candidate schedules. The first represents the empty
    "root" candidate that would normally be generated by DI. The next three
    represent the candidates returned by asking the first for its next
    generation. All three of them are stubbed to return an empty third
    generation. The first and third are stubbed to report themselves as
    impossible, while the second is stubbed to report itself as complete.

    As for our callbacks, they're also a mock. The mock callbacks object's
    stop condition is stubbed to return true iff it has seen a complete
    schedule.

    Taken together, a scheduling run with these mocks should cause a
    backtracking callback with the first "second generation" candidate,
    since it's impossible, then a complete callback with the second,
    since it's complete. There should be no further callbacks, since the stop
    condition is triggered by that single complete candidate.

    Note that these mocks are not particularly realistic. In particular, proper
    priority sorting would prevent an impossible candidate ever coming before
    a complete one... Don't let that throw you. The scheduler implementation
    should be robust enough to handle unusual schedules like that, and these
    tests are written for simplicity, not accurately modeling reality. Leave
    that for integration tests. ;)
     */

    // The empty "root" candidate.
    @Mock private SchedulerCandidate mockCandidateGen1;

    // "Second generation" candidates.
    @Mock private SchedulerCandidate mockCandidateGen2A;
    @Mock private SchedulerCandidate mockCandidateGen2B;
    @Mock private SchedulerCandidate mockCandidateGen2C;

    @Mock private Callbacks mockCallbacks;

    private SchedulerImpl instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Set up the stop condition.
        when(mockCallbacks.onCompleteResult(any())).thenReturn(true);

        final List<SchedulerCandidate> gen2 =
                Arrays.asList(mockCandidateGen2A, mockCandidateGen2B, mockCandidateGen2C);
        // The root candidate returns the second generation candidates.
        when(mockCandidateGen1.getNextGenerationCandidates()).then(unused -> gen2.stream());

        // The second generation candidates all return an empty stream.
        for(final SchedulerCandidate mockCandidate: gen2) {
            when(mockCandidate.getNextGenerationCandidates()).thenReturn(Stream.empty());
        }

        // The first and third "second generation" candidates are impossible.
        when(mockCandidateGen2A.isImpossible()).thenReturn(true);
        when(mockCandidateGen2C.isImpossible()).thenReturn(true);

        // The second is complete.
        when(mockCandidateGen2B.isComplete()).thenReturn(true);

        instance = new SchedulerImpl(mockCandidateGen1, mockCallbacks);
    }


    @Test
    void run_SawFirstBacktrack() {
        instance.run();
        verify(mockCallbacks).onBacktrack(mockCandidateGen2A);
    }

    @Test
    void run_SawCompleteSchedule() {
        instance.run();
        verify(mockCallbacks).onCompleteResult(mockCandidateGen2B);
    }

    @Test
    void run_StoppedAfterStopCondition() {
        instance.run();
        verify(mockCallbacks, never()).onBacktrack(mockCandidateGen2C);
    }

    @Test
    void run_CallsFailureCallbackOnFailedRun() {
        // Re-stub the callbacks' onCompleteResult() to always return false.
        // This ensures that the run will "fail".
        when(mockCallbacks.onCompleteResult(any())).thenReturn(false);
        instance.run();
        verify(mockCallbacks).onSchedulingFailed();
    }

    @Test
    void run_ShortCircuits() {
        // The scheduler implementation is supposed to short-circuit
        // evaluation of any outstanding candidates once the callbacks
        // return true. Since there can be thousands or MILLIONS of
        // outstanding candidates, this is a pretty important optimization...
        // To test it, we re-stub the second generation to be an infinite
        // stream of candidates that cause the callbacks to return true,
        // and make sure that the algorithm completes in finite time.
        when(mockCandidateGen1.getNextGenerationCandidates()).then(unused ->
                Stream.generate(() -> mockCandidateGen2B));
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> instance.run(),
                "Scheduling did not short-circuit after meeting stop condition.");
    }

}