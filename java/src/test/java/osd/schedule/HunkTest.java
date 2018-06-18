package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.input.Block;
import osd.database.input.Professor;
import osd.database.input.Room;
import osd.database.input.Section;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class HunkTest {

    @Mock private Section mockSection;
    @Mock private Block mockBlock;
    @Mock private Room mockRoom;
    @Mock private Professor mockProfessor;
    private Hunk instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new Hunk(mockSection, mockProfessor, mockRoom, Collections.singletonList(mockBlock));
    }

    @Test
    void getSection() {
        assertEquals(mockSection, instance.getSection());
    }

    @Test
    void getProfessor() {
        assertEquals(mockProfessor, instance.getProfessor());
    }

    @Test
    void getRoom() {
        assertEquals(mockRoom, instance.getRoom());
    }

    @Test
    void getBlocks() {
        assertEquals(Collections.singleton(mockBlock), instance.getBlocks());
    }

    @Test
    void testToString() {
        final String expected = "Hunk(" + mockSection
                + ", " + mockProfessor
                + ", " + mockRoom + ", "
                + Collections.singleton(mockBlock) + ")";
        final String result = instance.toString();
        assertEquals(expected, result);
    }

    @Test
    void equals() {
        final Hunk equalInstance = new Hunk(mockSection, mockProfessor, mockRoom, Collections.singletonList(mockBlock));
        assertTrue(instance.equals(equalInstance));
    }

    @Test
    void equals_UnequalWhenSectionDifferent() {
        final Hunk unequalInstance = new Hunk(mock(Section.class), mockProfessor, mockRoom, Collections.singletonList(mockBlock));
        assertFalse(instance.equals(unequalInstance));
    }

    @Test
    void equals_UnequalWhenProfessorDifferent() {
        final Hunk unequalInstance = new Hunk(mockSection, mock(Professor.class), mockRoom, Collections.singletonList(mockBlock));
        assertFalse(instance.equals(unequalInstance));
    }

    @Test
    void equals_UnequalWhenRoomDifferent() {
        final Hunk unequalInstance = new Hunk(mockSection, mockProfessor, mock(Room.class), Collections.singletonList(mockBlock));
        assertFalse(instance.equals(unequalInstance));
    }

    @Test
    void equals_UnequalWhenBlockDifferent() {
        final Hunk unequalInstance = new Hunk(mockSection, mockProfessor, mockRoom, Collections.singletonList(mock(Block.class)));
        assertFalse(instance.equals(unequalInstance));
    }
    @Test
    void testHashCode() {
        final int expected = Objects.hash(mockSection, mockProfessor, mockRoom, Collections.singleton(mockBlock));
        final int result = instance.hashCode();
        assertEquals(expected, result);
    }

    @Test
    void validateBlocks_TrueWhenNoneNull() {
        assertTrue(instance.validateBlocks());
    }

    @Test
    void validateBlocks_FalseWhenAtLeastOneNull() {
        instance = new Hunk(mockSection, mockProfessor, mockRoom, Arrays.asList(mockBlock, null));
        assertFalse(instance.validateBlocks());
    }
}