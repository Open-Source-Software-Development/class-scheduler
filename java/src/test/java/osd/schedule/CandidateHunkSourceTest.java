package osd.schedule;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.*;
import osd.output.Hunk;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CandidateHunkSourceTest {

    // Some bullshit test data.
    // We'll use the following rules for our mock constraints:
    // - For mockSection1, everything else in the hunk must contain a 1.
    // - For mockSection2, everything else in the hunk must contain a 2.
    // - For mockSection3, the room must contain a 1, but any block or
    //   either professor is okay.
    // This isn't a particularly realistic test case, but it keeps the
    // number of combinations sane.
    @Mock private Sources mockSources;
    @Mock private Professor mockProfessor1;
    @Mock private Professor mockProfessor2;
    @Mock private Section mockSection1;
    @Mock private Section mockSection2;
    @Mock private Section mockSection3;
    @Mock private Room mockRoom1;
    @Mock private Room mockRoom2;
    @Mock private Block mockBlockA1;
    @Mock private Block mockBlockA2;
    @Mock private Block mockBlockB1;
    @Mock private Block mockBlockB2;
    @Mock private Constraints mockConstraints;
    @Mock private Predicate<Hunk> mockBaseConstraints;

    private Hunk hunkForSection1;
    private CandidateHunkSource instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockBlockA1.getNext()).thenReturn(mockBlockA2);
        when(mockBlockA2.getPrevious()).thenReturn(mockBlockA1);
        when(mockBlockB1.getNext()).thenReturn(mockBlockB2);
        when(mockBlockB2.getPrevious()).thenReturn(mockBlockB1);
        when(mockBlockA1.getPairedWith()).thenReturn(mockBlockB1);
        when(mockBlockA2.getPairedWith()).thenReturn(mockBlockB2);
        when(mockSources.getBlocks()).thenAnswer((z) ->
                Stream.of(mockBlockA1, mockBlockA2, mockBlockB1, mockBlockB2));
        when(mockSources.getProfessors()).thenAnswer((z) ->
                Stream.of(mockProfessor1, mockProfessor2));
        when(mockSources.getRooms()).thenAnswer((z) ->
                Stream.of(mockRoom1, mockRoom2));
        when(mockSources.getSections()).thenAnswer((z) ->
                Stream.of(mockSection1, mockSection2, mockSection3));
        mockSources.getSections().forEach(section ->
                when(section.getBlockingStrategy()).thenReturn(this::mockBlockingStrategy));
        when(mockConstraints.test(any())).thenAnswer(a -> {
                final Hunk hunk = a.getArgument(0);
                final Section section = hunk.getSection();
                final Professor professor = hunk.getProfessor();
                final Room room = hunk.getRoom();
                final Set<Block> blocks = hunk.getBlocks();
                if (section.equals(mockSection3)) {
                    return (room == mockRoom1 || room == null);
                }
                if (section == mockSection1) {
                    return (professor == mockProfessor1 || professor == null)
                            && (room == mockRoom1 || room == null)
                            && (setEqualsSingletonOrNull(blocks, mockBlockA1, mockBlockB1));
                }
                if (section == mockSection2) {
                    return (professor == mockProfessor2 || professor == null)
                            && (room == mockRoom2 || room == null)
                            && (setEqualsSingletonOrNull(blocks, mockBlockA2, mockBlockB2));
                }
                throw new AssertionError();
            });
        when(mockConstraints.bindBaseConstraints(any())).thenReturn(mockBaseConstraints);
        when(mockBaseConstraints.test(any())).thenReturn(true);
        instance = new CandidateHunkSource(mockSources, mockConstraints);
        hunkForSection1 = new Hunk(mockSection1, mockProfessor1, mockRoom1, Collections.singletonList(mockBlockB2));
    }

    @Test
    void getExpectedHunks() {
        final int expected = (int)mockSources.getSections().count();
        final int result = instance.getExpectedHunks();
        assertEquals(expected, result);
    }

    @Test
    void candidates() {
        final Set<Hunk> expected = new HashSet<>(Arrays.asList(
                new Hunk(mockSection1, mockProfessor1, mockRoom1, Collections.singletonList(mockBlockA1)),
                new Hunk(mockSection1, mockProfessor1, mockRoom1, Collections.singletonList(mockBlockA2)),
                new Hunk(mockSection1, mockProfessor1, mockRoom1, Collections.singletonList(mockBlockB1)),
                new Hunk(mockSection1, mockProfessor1, mockRoom1, Collections.singletonList(mockBlockB2))
        ));
        final Set<Hunk> result = instance.getCandidateHunks(mockSection1).collect(Collectors.toSet());
        assertEquals(expected, result);
        for (final Hunk hunk: expected) {
            verify(mockBaseConstraints).test(hunk);
        }
    }

    @Test
    void candidates_RespectsAddHunk() {
        instance.onHunkAdded(hunkForSection1);
        final Set<Hunk> expected = new HashSet<>(Arrays.asList(
                // Professor 1 isn't available at block B2.
                new Hunk(mockSection3, mockProfessor1, mockRoom1, Collections.singletonList(mockBlockA1)),
                new Hunk(mockSection3, mockProfessor1, mockRoom1, Collections.singletonList(mockBlockA2)),
                new Hunk(mockSection3, mockProfessor1, mockRoom1, Collections.singletonList(mockBlockB1)),
                // Professor 2 is available at every block, but room 2 isn't
                // available block B2, so we get the same possibilities.
                new Hunk(mockSection3, mockProfessor2, mockRoom1, Collections.singletonList(mockBlockA1)),
                new Hunk(mockSection3, mockProfessor2, mockRoom1, Collections.singletonList(mockBlockA2)),
                new Hunk(mockSection3, mockProfessor2, mockRoom1, Collections.singletonList(mockBlockB1))
        ));
        final Set<Hunk> result = instance.getCandidateHunks(mockSection3).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void rebindConstructor_SameData() {
        final Set<Hunk> expected = instance.getCandidateHunks(mockSection2).collect(Collectors.toSet());
        final CandidateHunkSource copy = new CandidateHunkSource(instance, SchedulerLookups.empty());
        final Set<Hunk> result = copy.getCandidateHunks(mockSection2).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void rebindConstructor_ChangingCopyDoesntChangeOriginal() {
        final Set<Hunk> expected = instance.getCandidateHunks(mockSection2).collect(Collectors.toSet());
        final CandidateHunkSource copy = new CandidateHunkSource(instance, SchedulerLookups.empty());
        copy.onHunkAdded(hunkForSection1);
        final Set<Hunk> result = instance.getCandidateHunks(mockSection2).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    private Stream<Block> mockBlockingStrategy(final Block block) {
        return Stream.of(block);
    }

    private static boolean setEqualsSingletonOrNull(final Set<Block> set, final Block... candidates) {
        if (set == null) {
            return true;
        }
        for (final Block candidate: candidates) {
            if (set.equals(Collections.singleton(candidate))) {
                return true;
            }
        }
        return false;
    }

}