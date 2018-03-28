package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.*;
import osd.schedule.Hunk;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HunkFieldTest {

    @Mock private Course mockCourse;
    @Mock private Section mockSection;
    @Mock private Professor mockProfessor;
    @Mock private Room mockRoom;
    @Mock private Block mockBlock;
    @Mock private RoomType mockRoomType;

    @Mock private Hunk mockHunk;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSection.getCourse()).thenReturn(mockCourse);
        when(mockRoom.getRoomType()).thenReturn(mockRoomType);
        when(mockHunk.getProfessor()).thenReturn(mockProfessor);
        when(mockHunk.getSection()).thenReturn(mockSection);
        when(mockHunk.getRoom()).thenReturn(mockRoom);
        when(mockHunk.getBlocks()).thenReturn(new HashSet<>(Arrays.asList(mockBlock)));
    }

    @Test
    void extractSection_Inconclusive() {
        test(HunkField.Extraction.INCONCLUSIVE, mockSection, null, Hunk::getSection);
    }

    @Test
    void extractSection_Match() {
        test(HunkField.Extraction.MATCH, mockSection, mockSection, Hunk::getSection);
    }

    @Test
    void extractSection_No_Match() {
        test(HunkField.Extraction.NO_MATCH, mockSection, mock(Section.class), Hunk::getSection);
    }

    @Test
    void extractProfessor_Inconclusive() {
        test(HunkField.Extraction.INCONCLUSIVE, mockProfessor, null, Hunk::getProfessor);
    }

    @Test
    void extractProfessor_Match() {
        test(HunkField.Extraction.MATCH, mockProfessor, mockProfessor, Hunk::getProfessor);
    }

    @Test
    void extractProfessor_No_Match() {
        test(HunkField.Extraction.NO_MATCH, mockProfessor, mock(Professor.class), Hunk::getProfessor);
    }

    @Test
    void extractRoom_Inconclusive() {
        test(HunkField.Extraction.INCONCLUSIVE, mockRoom,null, Hunk::getRoom);
    }

    @Test
    void extractRoom_Match() {
        test(HunkField.Extraction.MATCH, mockRoom, mockRoom, Hunk::getRoom);
    }

    @Test
    void extractRoom_No_Match() {
        test(HunkField.Extraction.NO_MATCH, mockRoom, mock(Room.class), Hunk::getRoom);
    }

    @Test
    void extractCourse_Inconclusive() {
        test(HunkField.Extraction.INCONCLUSIVE, mockCourse, null, Hunk::getSection);
    }

    @Test
    void extractCourse_Match() {
        test(HunkField.Extraction.MATCH, mockCourse, mockSection, Hunk::getSection);
    }

    @Test
    void extractCourse_No_Match() {
        test(HunkField.Extraction.NO_MATCH, mockCourse,
                another(Section.class, Course.class, Section::getCourse), Hunk::getSection);
    }

    @Test
    void extractRoomType_Inconclusive() {
        test(HunkField.Extraction.INCONCLUSIVE, mockRoomType, null, Hunk::getRoom);
    }

    @Test
    void extractRoomType_Match() {
        test(HunkField.Extraction.MATCH, mockRoomType, mockRoom, Hunk::getRoom);
    }

    @Test
    void extractRoomType_No_Match() {
        test(HunkField.Extraction.NO_MATCH, mockRoomType,
                another(Room.class, RoomType.class, Room::getRoomType), Hunk::getRoom);
    }

    @Test
    void illegalArgumentException_OnUnknownClass() {
        final Object objectOfUnknownClass = this; // If there's an extractor for HunkFieldTest, Cthulhu did it.
        assertThrows(IllegalArgumentException.class, () -> HunkField.get(objectOfUnknownClass).getExtractor(objectOfUnknownClass));
    }

    private <T, U> T another(final Class<T> clazz, final Class<U> remote, final Function<T, U> getter) {
        final T t = mock(clazz);
        final U u = mock(remote);
        when(getter.apply(t)).thenReturn(u);
        return t;
    }

    private <T, U> void test(final HunkField.Extraction expected, final T lookFor,
                             final U value, final Function<Hunk, U> getter) {
        when(getter.apply(mockHunk)).thenReturn(value);
        final HunkField.Extraction result = HunkField.get(lookFor).getExtractor(lookFor).apply(mockHunk);
        assertEquals(expected, result);
    }

}