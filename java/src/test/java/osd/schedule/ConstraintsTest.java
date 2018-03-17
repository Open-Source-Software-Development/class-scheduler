package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.considerations.Constraint;
import osd.input.Professor;
import osd.input.Room;
import osd.input.Section;
import osd.output.Hunk;

import java.util.Arrays;
import java.util.Collections;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockConstraintAlwaysTrue.evaluate(any())).thenReturn(true);
        when(mockConstraintAlwaysFalse.evaluate(any())).thenReturn(false);
    }

    @Test
    void of() {
        final Constraints instance = new Constraints(Collections.singleton(mockConstraintAlwaysTrue));
        final boolean result = instance.test(mockHunk);
        verify(mockConstraintAlwaysTrue).evaluate(mockHunk);
        assertTrue(result);
    }

    @Test
    void of2() {
        final Constraints instance = new Constraints(Arrays.asList(mockConstraintAlwaysTrue, mockConstraintAlwaysFalse));
        final boolean result = instance.test(mockHunk);
        verify(mockConstraintAlwaysTrue).evaluate(mockHunk);
        verify(mockConstraintAlwaysFalse).evaluate(mockHunk);
        assertFalse(result);
    }

}