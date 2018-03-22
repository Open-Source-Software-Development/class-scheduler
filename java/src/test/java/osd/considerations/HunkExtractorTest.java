package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.input.*;
import osd.output.Hunk;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HunkExtractorTest {

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
        test(HunkExtractor.Extraction.INCONCLUSIVE, mockSection, null, Hunk::getSection);
    }

    @Test
    void extractSection_Match() {
        test(HunkExtractor.Extraction.MATCH, mockSection, mockSection, Hunk::getSection);
    }

    @Test
    void extractSection_No_Match() {
        test(HunkExtractor.Extraction.NO_MATCH, mockSection, mock(Section.class), Hunk::getSection);
    }

    @Test
    void extractProfessor_Inconclusive() {
        test(HunkExtractor.Extraction.INCONCLUSIVE, mockProfessor, null, Hunk::getProfessor);
    }

    @Test
    void extractProfessor_Match() {
        test(HunkExtractor.Extraction.MATCH, mockProfessor, mockProfessor, Hunk::getProfessor);
    }

    @Test
    void extractProfessor_No_Match() {
        test(HunkExtractor.Extraction.NO_MATCH, mockProfessor, mock(Professor.class), Hunk::getProfessor);
    }

    @Test
    void extractRoom_Inconclusive() {
        test(HunkExtractor.Extraction.INCONCLUSIVE, mockRoom,null, Hunk::getRoom);
    }

    @Test
    void extractRoom_Match() {
        test(HunkExtractor.Extraction.MATCH, mockRoom, mockRoom, Hunk::getRoom);
    }

    @Test
    void extractRoom_No_Match() {
        test(HunkExtractor.Extraction.NO_MATCH, mockRoom, mock(Room.class), Hunk::getRoom);
    }

    @Test
    void extractCourse_Inconclusive() {
        test(HunkExtractor.Extraction.INCONCLUSIVE, mockCourse, null, Hunk::getSection);
    }

    @Test
    void extractCourse_Match() {
        test(HunkExtractor.Extraction.MATCH, mockCourse, mockSection, Hunk::getSection);
    }

    @Test
    void extractCourse_No_Match() {
        test(HunkExtractor.Extraction.NO_MATCH, mockCourse,
                another(Section.class, Course.class, Section::getCourse), Hunk::getSection);
    }

    @Test
    void extractRoomType_Inconclusive() {
        test(HunkExtractor.Extraction.INCONCLUSIVE, mockRoomType, null, Hunk::getRoom);
    }

    @Test
    void extractRoomType_Match() {
        test(HunkExtractor.Extraction.MATCH, mockRoomType, mockRoom, Hunk::getRoom);
    }

    @Test
    void extractRoomType_No_Match() {
        test(HunkExtractor.Extraction.NO_MATCH, mockRoomType,
                another(Room.class, RoomType.class, Room::getRoomType), Hunk::getRoom);
    }

    @Test
    void illegalArgumentException_OnUnknownClass() {
        final Object objectOfUnknownClass = this; // If there's an extractor for HunkExtractorTest, Cthulhu did it.
        assertThrows(IllegalArgumentException.class, () ->
                HunkExtractor.of(objectOfUnknownClass));
    }

    private <T, U> T another(final Class<T> clazz, final Class<U> remote, final Function<T, U> getter) {
        final T t = mock(clazz);
        final U u = mock(remote);
        when(getter.apply(t)).thenReturn(u);
        return t;
    }

    private <T, U> void test(final HunkExtractor.Extraction expected, final T lookFor,
                             final U value, final Function<Hunk, U> getter) {
        when(getter.apply(mockHunk)).thenReturn(value);
        final HunkExtractor.Extraction result = HunkExtractor.of(lookFor).apply(mockHunk);
        assertEquals(expected, result);
    }

}