package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.output.Hunk;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PreferenceTest {

    @Mock private Predicate<Hunk> mockPredicate;
    @Mock private Hunk mockHunk;

    private final int worth = 7;

    private final Preference instance = new Preference() {

        @Override
        public boolean test(final Hunk hunk) {
            return mockPredicate.test(hunk);
        }

        @Override
        public int worth() {
            return worth;
        }
    };

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void evaluate_0OnFalse() {
        when(mockPredicate.test(any())).thenReturn(false);
        final int result = instance.evaluate(mockHunk);
        assertEquals(0, result);
        verify(mockPredicate).test(mockHunk);
    }

    @Test
    void evaluate_FullWorthOnTrue() {
        when(mockPredicate.test(any())).thenReturn(true);
        final int result = instance.evaluate(mockHunk);
        assertEquals(worth, result);
        verify(mockPredicate).test(mockHunk);
    }

}