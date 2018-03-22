package osd.considerations.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.considerations.Lookups;
import osd.input.Block;
import osd.input.Professor;
import osd.input.Section;
import osd.output.Hunk;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AvoidThreeConsecutiveBlocksTest {

    @Mock private Section mockSection;
    @Mock private Hunk mockHunk;
    @Mock private Professor mockProfessor;
    @Mock private Lookups mockLookups;
    private final Block[] mockBlocks = new Block[5];
    private final boolean[] isInLookup = new boolean[5];
    private final AvoidThreeConsecutiveBlocks instance = new AvoidThreeConsecutiveBlocks();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        setupMockBlocks();
        when(mockHunk.getProfessor()).thenReturn(mockProfessor);
        when(mockLookups.lookup(mockProfessor)).then(a ->
                IntStream.range(0, 5)
                        .filter(i -> isInLookup[i])
                        .mapToObj(i -> mockBlocks[i])
                        .map(b -> new Hunk(mockSection, mockProfessor, null, Collections.singletonList(b))));
    }

    @Test
    void worth() {
        assertTrue(instance.worth() < 0,
                "AvoidThreeConsecutiveBlocks should value itself at negative worth");
        // Story of my life...
    }

    @Test
    void bindPredicate_FalseOnEmptyLookup() {
        assertFalse(evaluatePredicate(2));
    }

    @Test
    void bindPredicate_FalseOnSingleAdjacentCourse() {
        addMockBlockToLookups(1);
        assertFalse(evaluatePredicate(2));
    }

    @Test
    void bindPredicate_TrueOnTwoAdjacentCourses() {
        addMockBlockToLookups(1);
        addMockBlockToLookups(3);
        assertTrue(evaluatePredicate(2));
    }

    @Test
    void bindPredicate_TrueWhenTwoCoursesBefore() {
        addMockBlockToLookups(0);
        addMockBlockToLookups(1);
        assertTrue(evaluatePredicate(2));
    }

    @Test
    void bindPredicate_TrueWhenTwoCoursesAfter() {
        addMockBlockToLookups(4);
        addMockBlockToLookups(3);
        assertTrue(evaluatePredicate(2));
    }

    private boolean evaluatePredicate(final int forWhichBlock) {
        final Set<Block> blocks = new HashSet<>(Arrays.asList(mockBlocks[forWhichBlock]));
        when(mockHunk.getBlocks()).thenReturn(blocks);
        return instance.bindPredicate(mockLookups).test(mockHunk);
    }

    private void addMockBlockToLookups(final int i) {
        isInLookup[i] = true;
    }

    private void setupMockBlocks() {
        Block mockBlock = null;
        for (int i = 0; i < mockBlocks.length; i++) {
            mockBlock = getNextMockBlock(mockBlock);
            mockBlocks[i] = mockBlock;
        }
    }

    private Block getNextMockBlock(final Block before) {
        final Block mockBlock = mock(Block.class);
        if (before != null) {
            when(mockBlock.getPrevious()).thenReturn(before);
            when(before.getNext()).thenReturn(mockBlock);
        }
        return mockBlock;
    }

}