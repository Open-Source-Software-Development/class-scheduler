package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.considerations.Preference;
import osd.input.Block;
import osd.input.Professor;
import osd.input.Room;
import osd.input.Section;
import osd.output.Callbacks;
import osd.output.Hunk;
import osd.output.Results;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SchedulerImplTest {

    // For our dummy schedule, we have two sections. Therefore, there are three
    // generations: the last being the liminal space after the final section
    // has been added, but the algorithm hasn't yet returned. Normally, these
    // would be generated automatically, but since we're mocking, we've got to
    // do it manually.
    @Mock private CandidateHunkPrioritizer mockCandidateHunkPrioritizerGen1;
    @Mock private CandidateHunkPrioritizer mockCandidateHunkPrioritizerGen2;
    @Mock private CandidateHunkPrioritizer mockCandidateHunkPrioritizerEmpty;

    // The two sections mentioned above.
    @Mock private Section mockSection1;
    @Mock private Section mockSection2;

    // Other dummy data.
    @Mock private Block mockBlock;
    @Mock private Professor mockProfessor; // Actually don't, that's rude.

    // We use these to make sure the algorithm considers candidates in preference order.
    @Mock private Room mockRoomPreferred;
    @Mock private Room mockRoomNotPreferred;
    @Mock private Preference mockPreference;

    @Mock private Callbacks mockCallbacks;
    private Results results;
    private Scheduler instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockCandidateHunkPrioritizerGen1.rebind(any())).thenReturn(mockCandidateHunkPrioritizerGen2);
        when(mockCandidateHunkPrioritizerGen1.getHighPrioritySection()).thenReturn(mockSection1);
        when(mockCandidateHunkPrioritizerGen1.getCandidateHunks(any())).then(i -> getCandidateHunkImpl(i.getArgument(0)));

        when(mockCandidateHunkPrioritizerGen2.rebind(any())).thenReturn(mockCandidateHunkPrioritizerEmpty);
        when(mockCandidateHunkPrioritizerGen2.getHighPrioritySection()).thenReturn(mockSection2);
        when(mockCandidateHunkPrioritizerGen2.getCandidateHunks(any())).then(i -> getCandidateHunkImpl(i.getArgument(0)));

        when(mockPreference.evaluate(any())).then(i -> preferenceImpl(i.getArgument(0)));
        Preferences preferences = new Preferences(Collections.singleton(mockPreference));

        when(mockCallbacks.stopCondition()).then(i -> results != null);
        doAnswer(i -> results = i.getArgument(0)).when(mockCallbacks).onCompleteResult(any());
        instance = new SchedulerImpl(mockCandidateHunkPrioritizerGen1, preferences);
    }

    @Test
    void run() {
        final List<Hunk> expected = Arrays.asList(
                new Hunk(mockSection1, mockProfessor, mockRoomPreferred, mockBlock),
                new Hunk(mockSection2, mockProfessor, mockRoomPreferred, mockBlock)
        );
        instance.run(mockCallbacks);
        assertNotNull(results);
        verify(mockCallbacks, atLeastOnce()).stopCondition();
        verify(mockCallbacks).onCompleteResult(results);
        final List<Hunk> result = results.getHunks();
        assertEquals(expected, result);
    }

    @Test
    void run_FalseOnFailure() {
        // A stop condition that can never be satisfied ensures the scheduler
        // will fail.
        when(mockCallbacks.stopCondition()).then(i -> false);
        assertFalse(instance.run(mockCallbacks));
        verify(mockCallbacks, atLeastOnce()).onBacktrack(any());
        verify(mockCallbacks, atLeastOnce()).onCompleteResult(any());
    }

    private int preferenceImpl(final Hunk hunk) {
        return mockRoomPreferred.equals(hunk.getRoom()) ? 1 : 0;
    }

    private Stream<Hunk> getCandidateHunkImpl(final Section section) {
        return Stream.of(
                new Hunk(section, mockProfessor, mockRoomPreferred, mockBlock),
                new Hunk(section, mockProfessor, mockRoomNotPreferred, mockBlock)
        );
    }

}