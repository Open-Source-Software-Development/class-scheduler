package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.RecordAccession;
import osd.database.input.Room;
import osd.database.input.Section;
import osd.database.input.record.UserConsiderationRecord;
import osd.schedule.Hunk;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserConsiderationTest {

    @Mock private Hunk mockHunk;
    @Mock private Section mockSection;
    @Mock private Section anotherMockSection;
    @Mock private Room mockRoom;
    @Mock private Room anotherMockRoom;

    private int id =(int)(Math.random() * 1000);
    private UserConsideration instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new UserConsideration(id, mockSection, mockRoom);
    }

    @Test
    void getMatch_Both() {
        when(mockHunk.getSection()).thenReturn(mockSection);
        when(mockHunk.getRoom()).thenReturn(mockRoom);
        final UserConsideration.Match expected = UserConsideration.Match.BOTH;
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        assertEquals(expected, result);
    }

    @Test
    void getMatch_Left() {
        when(mockHunk.getSection()).thenReturn(mockSection);
        when(mockHunk.getRoom()).thenReturn(anotherMockRoom);
        final UserConsideration.Match expected = UserConsideration.Match.LEFT;
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        assertEquals(expected, result);
    }

    @Test
    void getMatch_Right() {
        when(mockHunk.getSection()).thenReturn(anotherMockSection);
        when(mockHunk.getRoom()).thenReturn(mockRoom);
        final UserConsideration.Match expected = UserConsideration.Match.RIGHT;
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        assertEquals(expected, result);
    }

    @Test
    void getMatch_Neither() {
        when(mockHunk.getSection()).thenReturn(anotherMockSection);
        when(mockHunk.getRoom()).thenReturn(anotherMockRoom);
        final UserConsideration.Match expected = UserConsideration.Match.NEITHER;
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        assertEquals(expected, result);
    }

    @Test
    void getMatch_Null_Left() {
        when(mockHunk.getSection()).thenReturn(anotherMockSection);
        when(mockHunk.getRoom()).thenReturn(null);
        final UserConsideration.Match expected = UserConsideration.Match.INCONCLUSIVE;
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        assertEquals(expected, result);
    }

    @Test
    void getMatch_Null_Right() {
        when(mockHunk.getSection()).thenReturn(null);
        when(mockHunk.getRoom()).thenReturn(anotherMockRoom);
        final UserConsideration.Match expected = UserConsideration.Match.INCONCLUSIVE;
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        assertEquals(expected, result);
    }

    @Test
    void extract_IllegalArgumentExceptionOnUnknownField() {
        assertThrows(IllegalArgumentException.class, () ->
                new UserConsideration(id, (Object)null, null));
    }

    @Test
    void getId() {
        assertEquals(id, instance.getId());
    }

    @Test
    void recordConstructor() {
        final UserConsiderationRecord mockRecord = mock(UserConsiderationRecord.class);
        final RecordAccession mockRecordAccession = mock(RecordAccession.class);
        when(mockRecord.getLeftId()).thenReturn(0);
        when(mockRecord.getLeftTypeId()).thenReturn(1);
        when(mockRecord.getRightId()).thenReturn(2);
        when(mockRecord.getRightTypeId()).thenReturn(3);
        when(mockRecordAccession.getGeneric(mockRecord.getLeftTypeId(), mockRecord.getLeftId()))
                .thenReturn(mockSection);
        when(mockRecordAccession.getGeneric(mockRecord.getRightTypeId(), mockRecord.getRightId()))
                .thenReturn(mockRoom);
        final UserConsideration instance = new UserConsideration(mockRecord, mockRecordAccession);

        when(mockHunk.getSection()).thenReturn(mockSection);
        when(mockHunk.getRoom()).thenReturn(mockRoom);
        final UserConsideration.Match expected = UserConsideration.Match.BOTH;
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        assertEquals(expected, result);
    }

}