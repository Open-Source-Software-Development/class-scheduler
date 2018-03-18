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
import osd.output.Hunk;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class SchedulerTest {

    // For our dummy schedule, we have two sections. Therefore, there are three
    // generations: the last being the liminal space after the final section
    // has been added, but the algorithm hasn't yet returned. Normally, these
    // would be generated automatically, but since we're mocking, we've got to
    // do it manually.
    @Mock private Priority mockPriorityGen1;
    @Mock private Priority mockPriorityGen2;
    @Mock private Priority mockPriorityEmpty;

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

    private Scheduler instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockPriorityGen1.copy()).thenReturn(mockPriorityGen2);
        when(mockPriorityGen1.getHighPrioritySection()).thenReturn(mockSection1);
        when(mockPriorityGen1.getCandidateHunks(any())).then(i -> getCandidateHunkImpl(i.getArgument(0)));

        when(mockPriorityGen2.copy()).thenReturn(mockPriorityEmpty);
        when(mockPriorityGen2.getHighPrioritySection()).thenReturn(mockSection2);
        when(mockPriorityGen2.getCandidateHunks(any())).then(i -> getCandidateHunkImpl(i.getArgument(0)));

        when(mockPreference.evaluate(any())).then(i -> preferenceImpl(i.getArgument(0)));
        Preferences preferences = new Preferences(Collections.singleton(mockPreference));
        instance = new SchedulerImpl(mockPriorityGen1, preferences);
    }

    @Test
    void getResult() {
        final List<Hunk> expected = Arrays.asList(
                new Hunk(mockSection1, mockProfessor, mockRoomPreferred, mockBlock),
                new Hunk(mockSection2, mockProfessor, mockRoomPreferred, mockBlock)
        );
        final List<Hunk> result = instance.getResult();
        assertEquals(expected, result);
    }

    private int preferenceImpl(final Hunk hunk) {
        return mockRoomPreferred.equals(hunk.getRoom()) ? 1 : 0;
    }

    private <T extends Collection<Hunk>> T getHunksForSection(final Section section, final T result) {
        getCandidateHunkImpl(section).forEach(result::add);
        return result;
    }

    private Stream<Hunk> getCandidateHunkImpl(final Section section) {
        return Stream.of(
                new Hunk(section, mockProfessor, mockRoomPreferred, mockBlock),
                new Hunk(section, mockProfessor, mockRoomNotPreferred, mockBlock)
        );
    }

}