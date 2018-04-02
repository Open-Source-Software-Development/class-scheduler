package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.Room;
import osd.database.Section;
import osd.schedule.Hunk;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserConsiderationTest {

    @Mock private Hunk mockHunk;
    @Mock private Section mockSection;
    @Mock private Section anotherMockSection;
    @Mock private Room mockRoom;
    @Mock private Room anotherMockRoom;
    private UserConsideration instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new UserConsideration(mockSection, mockRoom);
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
                new UserConsideration(null, null));
    }

}