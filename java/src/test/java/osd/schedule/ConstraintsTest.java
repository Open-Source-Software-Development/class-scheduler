package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.considerations.BaseConstraint;
import osd.considerations.Constraint;
import osd.considerations.Lookups;
import osd.input.Professor;
import osd.input.Room;
import osd.input.Section;
import osd.output.Hunk;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConstraintsTest {

    @Mock private Section mockSection;
    @Mock private Professor mockProfessor;
    @Mock private Room mockRoom;
    @Mock private Constraint mockConstraintAlwaysTrue;
    @Mock private Constraint mockConstraintAlwaysFalse;
    @Mock private Hunk mockHunk;

    @Mock private BaseConstraint mockBaseConstraint;
    @Mock private Lookups mockLookups;
    @Mock private Predicate<Hunk> mockBaseConstraintImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockConstraintAlwaysTrue.test(any())).thenReturn(true);
        when(mockConstraintAlwaysFalse.test(any())).thenReturn(false);
        when(mockBaseConstraint.bindPredicate(mockLookups)).thenReturn(mockBaseConstraintImpl);
        when(mockBaseConstraint.bind(mockLookups)).thenCallRealMethod();
    }

    @Test
    void test() {
        final Constraints instance = new Constraints(
                Collections.singleton(mockConstraintAlwaysTrue),
                Collections.emptyList());
        final boolean result = instance.test(mockHunk);
        verify(mockConstraintAlwaysTrue).test(mockHunk);
        assertTrue(result);
    }

    @Test
    void test2() {
        final Constraints instance = new Constraints(
                Arrays.asList(mockConstraintAlwaysTrue, mockConstraintAlwaysFalse),
                Collections.emptyList());
        final boolean result = instance.test(mockHunk);
        verify(mockConstraintAlwaysTrue).test(mockHunk);
        verify(mockConstraintAlwaysFalse).test(mockHunk);
        assertFalse(result);
    }

    @Test
    void bindBaseConstraints() {
        final Constraints instance = new Constraints(Collections.emptyList(),
                Collections.singleton(mockBaseConstraint));
        final boolean result = instance.bindBaseConstraints(mockLookups).test(mockHunk);
        assertFalse(result);
        verify(mockBaseConstraint).bind(mockLookups);
        verify(mockBaseConstraintImpl).test(mockHunk);
    }

    @Test
    void professorPredicate() {
        final Constraints instance = new Constraints(
                Collections.singleton(mockConstraintAlwaysTrue),
                Collections.emptyList());
        final Predicate<Professor> predicate = Constraints.professorPredicate(instance, mockSection);
        final boolean result = predicate.test(mockProfessor);
        // Because we used mockConstraintAlwaysTrue.
        assertTrue(result);
        verify(mockConstraintAlwaysTrue).test(new Hunk(mockSection, mockProfessor, null, null));
    }

    @Test
    void roomPredicate() {
        final Constraints instance = new Constraints(
                Collections.singleton(mockConstraintAlwaysTrue),
                Collections.emptyList());
        final Predicate<Room> predicate = Constraints.roomPredicate(instance, mockSection);
        final boolean result = predicate.test(mockRoom);
        // Because we used mockConstraintAlwaysTrue.
        assertTrue(result);
        verify(mockConstraintAlwaysTrue).test(new Hunk(mockSection, null, mockRoom, null));
    }

}