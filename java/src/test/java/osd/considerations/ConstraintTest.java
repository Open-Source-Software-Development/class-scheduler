package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.schedule.Hunk;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConstraintTest {

    @Mock private Constraint mockConstraintTrue;
    @Mock private Constraint mockConstraintFalse;
    @Mock private Hunk mockHunk;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockConstraintTrue.test(any())).thenReturn(false);
        when(mockConstraintTrue.and(any())).thenCallRealMethod();
        when(mockConstraintTrue.or(any())).thenCallRealMethod();
        when(mockConstraintFalse.test(any())).thenReturn(true);
        when(mockConstraintFalse.and(any())).thenCallRealMethod();
        when(mockConstraintFalse.or(any())).thenCallRealMethod();
    }

    @Test
    void and() {
        final boolean result = mockConstraintTrue.and(mockConstraintFalse).test(mockHunk);
        assertFalse(result);
    }

    @Test
    void or() {
        final boolean result = mockConstraintTrue
                .or(mockConstraintFalse)
                .test(mockHunk);
        assertTrue(result);
        // Since or can't short-circuit in this case,
        // we expect both to be called.
        verify(mockConstraintFalse).test(mockHunk);
        verify(mockConstraintTrue).test(mockHunk);
    }

    @Test
    void dummyAnd() {
        // The dummy should always just return the other instance.
        assertEquals(mockConstraintFalse, Constraint.DUMMY.and(mockConstraintFalse));
    }

    @Test
    void dummyOr() {
        // The dummy should always just return the other instance.
        assertEquals(mockConstraintTrue, Constraint.DUMMY.or(mockConstraintTrue));
    }

    @Test
    void dummyTest() {
        assertTrue(Constraint.DUMMY.test(mockHunk));
    }

}