package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SchedulerCandidateTest {

    /* HOW THIS TEST WORKS:
    We have two mock sections. We also have two mock professors, two mock
    blocks, and two mock rooms. mockSection1 can only be taught by
    mockProfessor1. mockSection2 can be taught by either mock professor, but
    mockProfessor2 is preferred. mockSection1 can only go in mockRoom1 at
    mockBlock1, and mockSection2 can only go in mockRoom2 and mockBlock2.

    Given these sources, constraints, and
    preferences, we know exactly what should happen if we were to run the
    scheduling algorithm. Our initial empty candidate schedule has one child,
    containing mockSection1. That child has two children, containing
    mockSection2. The child taught by mockProfessor2 comes first. Both children
    are complete.
     */

    private final List<Section> sections = new ArrayList<>();
    private final List<Block> blocks = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();
    private final List<Professor> professors = new ArrayList<>();

    @Mock private Course mockCourse1, mockCourse2;
    @Mock private Section mockSection1, mockSection2;
    @Mock private Professor mockProfessor1, mockProfessor2;
    @Mock private Room mockRoom1, mockRoom2;
    @Mock private Block mockBlock1, mockBlock2;

    @Mock private Sources mockSources;
    @Mock private Considerations mockConsiderations;

    private SchedulerCandidate instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSources.getSections()).then(unused -> sections.stream());
        when(mockSources.getBlocks()).then(unused -> blocks.stream());
        when(mockSources.getRooms()).then(unused -> rooms.stream());
        when(mockSources.getProfessors()).then(unused -> professors.stream());

        // mockSection2 can be taught by either professor, mockSection1 needs mockProfessor1.
        when(mockConsiderations.getUserConstraints()).thenReturn(hunk -> {
            final Room room = hunk.getRoom();
            final Set<Block> blocks = hunk.getBlocks();
            final Professor professor = hunk.getProfessor();
            final Course course = hunk.getSection().getCourse();
            if (room != null) {
                if (course == mockCourse1 && room != mockRoom1) {
                    return false;
                }
                if (course == mockCourse2 && room != mockRoom2) {
                    return false;
                }
            }
            if (blocks != null && !blocks.isEmpty()) {
                final Block block = blocks.iterator().next();
                if (course == mockCourse1 && block != mockBlock1) {
                    return false;
                }
                if (course == mockCourse2 && block != mockBlock2) {
                    return false;
                }
            }
            return professor == null || professor == mockProfessor1 || course == mockCourse2;
        });

        // mockProfessor2 is preferred.
        when(mockConsiderations.getPreferenceComparator(any())).thenReturn(
                Comparator.comparingInt(hunk -> (hunk.getProfessor() == mockProfessor2 ? 0 : 1))
        );

        // No base constraints.
        when(mockConsiderations.getBaseConstraintPredicate(any())).thenReturn(hunk -> true);

        when(mockCourse1.getBlockingStrategy()).thenReturn(Stream::of);
        when(mockCourse2.getBlockingStrategy()).thenReturn(Stream::of);
        when(mockSection1.getCourse()).thenReturn(mockCourse1);
        when(mockSection2.getCourse()).thenReturn(mockCourse2);

        sections.add(mockSection1);
        sections.add(mockSection2);
        professors.add(mockProfessor1);
        professors.add(mockProfessor2);
        rooms.add(mockRoom1);
        rooms.add(mockRoom2);
        blocks.add(mockBlock1);
        blocks.add(mockBlock2);

        instance = new SchedulerCandidate(mockSources, mockConsiderations);
    }

    @Test
    void getNextGenerationCandidates_ChoosesRightCourse() {
        instance.getNextGenerationCandidates()
                .map(SchedulerCandidate::getHunks)
                .flatMap(List::stream)
                .forEach(hunk -> assertEquals(mockSection1, hunk.getSection()));
    }

    @Test
    void getNextGenerationCandidates_SortsInPreferenceOrder() {
        advanceToNextGeneration();
        // The second generation should be for mockSection2. Either mock
        // professor can teach that, but there's a preference for
        // mockProfessor2. Therefore, we expect to see professors
        // in that order.
        final List<Professor> expected = Arrays.asList(mockProfessor2, mockProfessor1);
        final List<Professor> professors = instance.getNextGenerationCandidates()
                .map(SchedulerCandidate::getHunks)
                .flatMap(List::stream)
                .filter(hunk -> hunk.getSection() == mockSection2)
                .map(Hunk::getProfessor)
                .collect(Collectors.toList());
        assertEquals(expected, professors);
    }

    @Test
    void isComplete_InitiallyFalse() {
        assertFalse(instance.isComplete());
    }

    @Test
    void isComplete_FalseWhenNotComplete() {
        advanceToNextGeneration();
        assertFalse(instance.isComplete());
    }

    @Test
    void isComplete_TrueWhenComplete() {
        advanceToNextGeneration();
        advanceToNextGeneration();
        assertTrue(instance.isComplete());
    }

    @Test
    void isImpossible_InitiallyFalse() {
        assertFalse(instance.isImpossible());
    }

    @Test
    void isImpossible_FalseWhenPossible() {
        advanceToNextGeneration();
        assertFalse(instance.isImpossible());
    }

    @Test
    void isImpossible_TrueWhenImpossible() {
        // Make the schedule impossible by removing all the blocks.
        blocks.clear();
        regenerateInstance();
        assertTrue(instance.isImpossible());
    }

    @Test
    void getExpectedHunkCount() {
        assertEquals(2, instance.getExpectedHunkCount());
    }

    @Test
    void getExpectedHunkCount_AfterGeneration() {
        advanceToNextGeneration();
        assertEquals(2, instance.getExpectedHunkCount());
    }

    private void regenerateInstance() {
        instance = new SchedulerCandidate(mockSources, mockConsiderations);
    }

    private void advanceToNextGeneration() {
        instance = instance.getNextGenerationCandidates().iterator().next();
    }

}