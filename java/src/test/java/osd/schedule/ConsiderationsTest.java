package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsiderationsTest {

    /* HOW THIS TEST WORKS:
    We have three methods, and each returns something that, in turn, returns
    a binary outcome. We mock the underlying objects, stub them to return one
    outcome, then the other, and make sure we see the actual result in the
    instance's returned versions. Mockito verification seals the deal.
     */

    @Mock private Predicate<Hunk> mockUserConstraints;
    @Mock private BiPredicate<Lookups, Hunk> mockBaseConstraints;
    @Mock private BiFunction<Lookups, Hunk, Integer> mockPreferences;

    @Mock private Lookups mockLookups;
    @Mock private Hunk mockHunk, anotherMockHunk;
    private Considerations instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new Considerations(mockUserConstraints, mockBaseConstraints, mockPreferences);
        when(mockPreferences.apply(mockLookups, mockHunk)).thenReturn(0);
        when(mockPreferences.apply(mockLookups, anotherMockHunk)).thenReturn(1, -1);
        when(mockBaseConstraints.test(mockLookups, mockHunk)).thenReturn(true, false);
        when(mockUserConstraints.test(mockHunk)).thenReturn(true, false);
    }

    @Test
    void getPreferenceComparator() {
        final Comparator<Hunk> comparator = instance.getPreferenceComparator(mockLookups);
        final int result1 = comparator.compare(mockHunk, anotherMockHunk);
        assertTrue(result1 > 0);
        verify(mockPreferences).apply(mockLookups, mockHunk);
        verify(mockPreferences).apply(mockLookups, anotherMockHunk);
        final int result2 = comparator.compare(mockHunk, anotherMockHunk);
        assertTrue(result2 < 0);
    }

    @Test
    void getUserConstraints() {
        final Predicate<Hunk> predicate = instance.getUserConstraints();
        final boolean result1 = predicate.test(mockHunk);
        assertTrue(result1);
        verify(mockUserConstraints).test(mockHunk);
        final boolean result2 = predicate.test(mockHunk);
        assertFalse(result2);
    }

    @Test
    void getBaseConstraintPredicate() {
        final Predicate<Hunk> predicate = instance.getBaseConstraints(mockLookups);
        final boolean result1 = predicate.test(mockHunk);
        assertTrue(result1);
        verify(mockBaseConstraints).test(mockLookups, mockHunk);
        final boolean result2 = predicate.test(mockHunk);
        assertFalse(result2);
    }

}