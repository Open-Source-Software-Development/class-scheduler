package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SchedulerStateTest {

    /* HOW THIS TEST WORKS:
    We have four mock courses. All use a blocking strategy that returns a
    block and its paired block. We have two pairs of blocks, three professors,
    and three rooms. mockCourse1 can be taught only by mockProfessor1 in
    mockRoom1. mockCourse2 can be taught by mockProfessor1 or mockProfessor2,
    but only in mockRoom2, while mockCourse3 can be taught only by
    mockProfessor2 in mockRoom1 or mockRoom2. mockCourse4 can only be taught by
    mockProfessor3 in mockRoom3. Any course can be taught at any block, except
    mockCourse1 can only be taught block 1.

    Therefore, we expect mockCourse1 to be "adjacent" to mockCourse2 and
    mockCourse3. After scheduling a course for mockCourse1, there should be
    three possibilities left for either of them.

    To test sorting, we prefer hunks using mockBlock1A/mockBlock1B to those
    using mockBlock2A/mockBlock2B.
     */

    @Mock private Sources mockSources;
    @Mock private Course mockCourse1, mockCourse2, mockCourse3, mockCourse4;
    @Mock private Section mockSection1, mockSection2, mockSection3, mockSection4;
    @Mock private Professor mockProfessor1, mockProfessor2, mockProfessor3;
    @Mock private Room mockRoom1, mockRoom2, mockRoom3;
    @Mock private Block mockBlock1A, mockBlock1B, mockBlock2A, mockBlock2B;

    @Mock private Considerations mockConsiderations;

    private final Function<Block, Stream<Block>> blockingStrategy =
            block -> Stream.of(block, block.getPairedWith());
    // See constraint explanation above.
    private final Predicate<Hunk> userConstraints =
            hunk -> {
                final Section section = hunk.getSection();
                final Professor professor = hunk.getProfessor();
                final Room room = hunk.getRoom();
                final Set<Block> blocks = hunk.getBlocks();
                if (section == mockSection1) {
                    if (professor != null && professor != mockProfessor1) {
                        return false;
                    }
                    if (room != null && room != mockRoom1) {
                        return false;
                    }
                    if (blocks != null && blocks.contains(mockBlock2A)) {
                        return false;
                    }
                }
                if (section == mockSection2) {
                    if (professor == mockProfessor3) {
                        return false;
                    }
                    if (room != null && room != mockRoom2) {
                        return false;
                    }
                }
                if (section == mockSection3) {
                    if (room == mockRoom3) {
                        return false;
                    }
                    if (professor != null && professor != mockProfessor2) {
                        return false;
                    }
                }
                if (section == mockSection4) {
                    if (professor != null && professor != mockProfessor3) {
                        return false;
                    }
                    if (room != null && room != mockRoom3) {
                        return false;
                    }
                }
                return true;
            };
    // Prefer block 1 to block 2.
    private final Comparator<Hunk> preferences =
            Comparator.comparing(hunk -> hunk.getBlocks().contains(mockBlock1A) ? 0 : 1);

    private SchedulerState root, withSection1Hunk ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSection1.getCourse()).thenReturn(mockCourse1);
        when(mockCourse1.getBlockingStrategy()).thenReturn(blockingStrategy);
        when(mockSection2.getCourse()).thenReturn(mockCourse2);
        when(mockCourse2.getBlockingStrategy()).thenReturn(blockingStrategy);
        when(mockSection3.getCourse()).thenReturn(mockCourse3);
        when(mockCourse3.getBlockingStrategy()).thenReturn(blockingStrategy);
        when(mockSection4.getCourse()).thenReturn(mockCourse4);
        when(mockCourse4.getBlockingStrategy()).thenReturn(blockingStrategy);
        when(mockConsiderations.getUserConstraints()).thenReturn(userConstraints);
        when(mockConsiderations.getBaseConstraints(any())).thenReturn(hunk -> true);
        when(mockConsiderations.getPreferenceComparator(any())).thenReturn(preferences);

        when(mockBlock1A.getPairedWith()).thenReturn(mockBlock1B);
        when(mockBlock1B.getPairedWith()).thenReturn(mockBlock1A);
        when(mockBlock2A.getPairedWith()).thenReturn(mockBlock2B);
        when(mockBlock2B.getPairedWith()).thenReturn(mockBlock2A);

        when(mockSources.getSections()).then(unused -> Stream.of(mockSection1, mockSection2, mockSection3, mockSection4));
        when(mockSources.getProfessors()).then(unused -> Stream.of(mockProfessor1, mockProfessor2, mockProfessor3));
        when(mockSources.getRooms()).then(unused -> Stream.of(mockRoom1, mockRoom2, mockRoom3));
        when(mockSources.getBlocks()).then(unused -> Stream.of(mockBlock1A, mockBlock1B, mockBlock2A, mockBlock2B));

        root = new SchedulerState(mockSources, mockConsiderations);
        withSection1Hunk = root.childStatesForSection(mockSection1).iterator().next();
    }

    @Test
    void countCandidates() {
        assertEquals(4, root.countCandidates(mockSection2));
    }

    @Test
    void countCandidates_AfterSomethingAdded() {
        assertEquals(3, withSection1Hunk.countCandidates(mockSection2));
    }

    @Test
    void childStatesForSection_FindsEverything() {
        final Set<Hunk> expected = new HashSet<>(Arrays.asList(
                new Hunk(mockSection2, mockProfessor1, mockRoom2, Arrays.asList(mockBlock1A, mockBlock1B)),
                new Hunk(mockSection2, mockProfessor1, mockRoom2, Arrays.asList(mockBlock2A, mockBlock2B)),
                new Hunk(mockSection2, mockProfessor2, mockRoom2, Arrays.asList(mockBlock1A, mockBlock1B)),
                new Hunk(mockSection2, mockProfessor2, mockRoom2, Arrays.asList(mockBlock2A, mockBlock2B))
        ));
        final Set<Hunk> result = root.childStatesForSection(mockSection2)
                .map(state -> state.recentHunk)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void childStatesForSection_AfterSomethingAdded() {
        final Set<Hunk> expected = new HashSet<>(Arrays.asList(
                new Hunk(mockSection2, mockProfessor1, mockRoom2, Arrays.asList(mockBlock2A, mockBlock2B)),
                new Hunk(mockSection2, mockProfessor2, mockRoom2, Arrays.asList(mockBlock1A, mockBlock1B)),
                new Hunk(mockSection2, mockProfessor2, mockRoom2, Arrays.asList(mockBlock2A, mockBlock2B))
        ));
        final Set<Hunk> result = withSection1Hunk.childStatesForSection(mockSection2)
                .map(state -> state.recentHunk)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void childStatesForSection_Sorted() {
        final List<Hunk> expected = Arrays.asList(
                new Hunk(mockSection4, mockProfessor3, mockRoom3, Arrays.asList(mockBlock1A, mockBlock1B)),
                new Hunk(mockSection4, mockProfessor3, mockRoom3, Arrays.asList(mockBlock2A, mockBlock2B))
        );
        final List<Hunk> result = root.childStatesForSection(mockSection4)
                .map(state -> state.recentHunk)
                .collect(Collectors.toList());
        assertEquals(expected, result);
    }

    @Test
    void getAdjacent() {
        final Set<Course> expected = new HashSet<>(Arrays.asList(mockCourse2, mockCourse3));
        final Set<Course> result = root.getAdjacent(mockCourse1)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void lookupAllHunks() {
        final List<Hunk> expected = Collections.singletonList(
                new Hunk(mockSection1, mockProfessor1, mockRoom1, Arrays.asList(mockBlock1A, mockBlock1B)));
        final List<Hunk> result = withSection1Hunk.lookupAllHunks()
                .collect(Collectors.toList());
        assertEquals(expected, result);
    }

    @Test
    void lookup_ByProfessor() {
        final List<Hunk> expected = Collections.singletonList(
                new Hunk(mockSection1, mockProfessor1, mockRoom1, Arrays.asList(mockBlock1A, mockBlock1B)));
        final List<Hunk> result = withSection1Hunk.lookup(mockProfessor1)
                .collect(Collectors.toList());
        assertEquals(expected, result);
    }

    @Test
    void lookup_ByProfessor_EmptyStreamWhenNothingScheduled() {
        assertEquals(0, withSection1Hunk.lookup(mockProfessor3).count());
    }

    @Test
    void lookup_BySection() {
        final Hunk expected =
                new Hunk(mockSection1, mockProfessor1, mockRoom1, Arrays.asList(mockBlock1A, mockBlock1B));
        final Hunk result = withSection1Hunk.lookup(mockSection1);
        assertEquals(expected, result);
    }

}