package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import osd.input.*;
import osd.output.Hunk;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserConsiderationTest {

    private static final class UserConsiderationTestImpl extends UserConsideration {

        <L, R> UserConsiderationTestImpl(Class<L> leftClass, L left, Class<R> rightClass, R right) {
            super(leftClass, left, rightClass, right);
        }

        @Override
        public boolean test(final Hunk hunk) {
            return true;
        }
    }

    @Mock private Hunk mockHunk;
    @Mock private Section mockSection;
    @Mock private Section anotherMockSection;
    @Mock private Room mockRoom;
    @Mock private Room anotherMockRoom;
    private UserConsideration instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new UserConsiderationTestImpl(Section.class, mockSection, Room.class, mockRoom);
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
    void getMatch_One() {
        when(mockHunk.getSection()).thenReturn(mockSection);
        when(mockHunk.getRoom()).thenReturn(anotherMockRoom);
        final UserConsideration.Match expected = UserConsideration.Match.ONE;
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
        final UserConsideration.Match expected = UserConsideration.Match.NULL;
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        assertEquals(expected, result);
    }

    @Test
    void getMatch_Null_Right() {
        when(mockHunk.getSection()).thenReturn(null);
        when(mockHunk.getRoom()).thenReturn(anotherMockRoom);
        final UserConsideration.Match expected = UserConsideration.Match.NULL;
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        assertEquals(expected, result);
    }

    @Test
    void extract_Section() {
        testExtractionSimple(Section.class, Hunk::getSection);
    }

    @Test
    void extract_Room() {
        testExtractionSimple(Room.class, Hunk::getRoom);
    }

    @Test
    void extract_Block() {
        testExtractionSimple(Block.class, Hunk::getBlock);
    }

    @Test
    void extract_Professor() {
        testExtractionSimple(Professor.class, Hunk::getProfessor);
    }

    @Test
    void extract_RoomType() {
        final RoomType mockRoomType = mock(RoomType.class);
        when(mockRoom.getRoomType()).thenReturn(mockRoomType);
        when(mockHunk.getRoom()).thenReturn(mockRoom);
        testExtractionComplex(RoomType.class, mockRoomType);
    }

    @Test
    void extract_Course() {
        final Course mockCourse = mock(Course.class);
        when(mockSection.getCourse()).thenReturn(mockCourse);
        when(mockHunk.getSection()).thenReturn(mockSection);
        testExtractionComplex(Course.class, mockCourse);
    }

    @Test
    void extract_IllegalArgumentExceptionOnUnknownField() {
        final Class<?> unknownField = UserConsiderationTest.class;
        final IllegalArgumentException e =
                assertThrows(IllegalArgumentException.class, () ->
                        new UserConsiderationTestImpl(unknownField, null, unknownField, null)
                                .test(mockHunk));
        assertTrue(e.getLocalizedMessage().contains(unknownField.getCanonicalName()));
    }

    // Helper to test extraction of fields directly present on a hunk.
    private <T> void testExtractionSimple(final Class<T> field, final Function<Hunk, T> getter) {
        final T mock = Mockito.mock(field);
        when(getter.apply(mockHunk)).thenReturn(mock);
        clearInvocations(mockHunk);
        final UserConsideration instance = new UserConsiderationTestImpl(field, mock, field, mock);
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        // Since we... kinda used the same mock for both our fields, it better return both.
        assertEquals(UserConsideration.Match.BOTH, result);
        getter.apply(verify(mockHunk, times((2))));
    }

    // Helper to test extraction of fields that can be derived from a hunk,
    // but aren't directly present (eg. room type can be derived from room).
    private <T> void testExtractionComplex(final Class<T> field, final T mock) {
        clearInvocations(mockHunk);
        final UserConsideration instance = new UserConsiderationTestImpl(field, mock, field, mock);
        final UserConsideration.Match result = instance.getMatch(mockHunk);
        assertEquals(UserConsideration.Match.BOTH, result);
    }

}