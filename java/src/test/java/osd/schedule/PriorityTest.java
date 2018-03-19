package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import osd.input.Sources;
import osd.input.*;
import osd.output.Hunk;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriorityTest {

    @Mock private Sources mockSources;
    @Mock private Constraints mockConstraints;
    @Mock private Predicate<Hunk> mockBaseConstraints;

    @Mock private Section mockSection1;
    @Mock private Section mockSection2;
    @Mock private Section mockSection3;

    @Mock private Professor mockProfessor1;
    @Mock private Professor mockProfessor2;

    @Mock private Block mockBlock;

    @Mock private Room mockRoom1;
    @Mock private Room mockRoom2;

    private Hunk hunkForSection1;
    private Hunk hunkForSection2;
    private Hunk hunkForSection3;

    private Priority instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSources.getSections()).thenAnswer(a ->
                Stream.of(mockSection1, mockSection2, mockSection3));
        when(mockSources.getProfessors()).thenAnswer(a ->
                Stream.of(mockProfessor1, mockProfessor2));
        when(mockSources.getRooms()).thenAnswer(a ->
                Stream.of(mockRoom1, mockRoom2));
        when(mockSources.getBlocks()).thenAnswer(a -> Stream.of(mockBlock));
        when(mockConstraints.test(any())).then(this::mockConstraintsImpl);
        when(mockConstraints.bindBaseConstraints(any())).thenReturn(mockBaseConstraints);
        when(mockBaseConstraints.test(any())).thenReturn(true);

        instance = new Priority(mockSources, mockConstraints);
        hunkForSection1 = new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlock);
        hunkForSection2 = new Hunk(mockSection2, mockProfessor2, mockRoom2, mockBlock);
        hunkForSection3 = new Hunk(mockSection3, mockProfessor1, mockRoom1, mockBlock);
    }

    @Test
    void getHighPrioritySection_Initial() {
        assertEquals(mockSection1, instance.getHighPrioritySection());
    }

    @Test
    void getHighPrioritySection_AfterOneAddition() {
        instance.onHunkAdded(hunkForSection1);
        assertEquals(mockSection2, instance.getHighPrioritySection());
    }

    @Test
    void getHighPrioritySection_AfterTwoAdditions() {
        getHighPrioritySection_AfterOneAddition();
        instance.onHunkAdded(hunkForSection2);
        assertEquals(mockSection3, instance.getHighPrioritySection());
    }

    @Test
    void getHighPrioritySection_AfterEverythingAdded() {
        getHighPrioritySection_AfterTwoAdditions();
        instance.onHunkAdded(hunkForSection3);
        assertNull(instance.getHighPrioritySection());
    }

    @Test
    void onHunkAdded_FailsOnUnexpectedSection() {
        final Section unexpectedMockSection = mock(Section.class);
        final Hunk hunkWithUnexpectedSection = new Hunk(unexpectedMockSection, null, null, null);
        assertThrows(NoSuchElementException.class, () -> instance.onHunkAdded(hunkWithUnexpectedSection));
    }

    @Test
    void rebind_SameData() {
        final Section expected = instance.getHighPrioritySection();
        final Priority copy = instance.rebind(Results.empty());
        final Section result = copy.getHighPrioritySection();
        assertEquals(expected, result);
    }

    @Test
    void rebind_ChangingCopyDoesntChangeOriginal() {
        final Section expected = instance.getHighPrioritySection();
        final Priority copy = instance.rebind(Results.empty());
        copy.onHunkAdded(hunkForSection1);
        final Section result = instance.getHighPrioritySection();
        assertEquals(expected, result);
    }

    private boolean mockConstraintsImpl(final InvocationOnMock invocation) {
        final Hunk hunk = invocation.getArgument(0);
        return mockConstraintsImpl(hunk);
    }

    private boolean mockConstraintsImpl(final Hunk hunk) {
        // Structure our mock constraints such that mockSection1 has the
        // fewest possibilities and mockSection3 has the most.
        final Section section = hunk.getSection();
        if (section == null) {
            return true;
        }
        final Professor professor = hunk.getProfessor();
        final Room room = hunk.getRoom();
        final Block block = hunk.getBlock();
        // Most specific constraints: mockSection1 needs exactly one
        // specific combination.
        if (mockSection1.equals(section)) {
            return equalsOrNull(professor, mockProfessor1)
                    && equalsOrNull(room, mockRoom1)
                    && equalsOrNull(block, mockBlock);
        }
        // Less specific constraints: mockSection2 needs one of
        // two combinations.
        if (mockSection2.equals(section)){
            return equalsOrNull(professor, mockProfessor1)
                    && equalsOrNull(room, mockRoom1, mockRoom2)
                    && equalsOrNull(block, mockBlock);
        }
        // Least specific: Anything goes!
        return true;
    }

    @SafeVarargs
    private static <T> boolean equalsOrNull(final T t, final T... options) {
        return t == null || Arrays.stream(options).anyMatch(t::equals);
    }

}