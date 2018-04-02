package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.schedule.Hunk;
import osd.schedule.Lookups;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BasePreferenceTest {

    @Mock private Lookups mockLookups;
    @Mock private Predicate<Hunk> mockPredicate;
    @Mock private Hunk mockHunk;
    private final int worth = 7;

    private final BasePreference instance = new BasePreference() {
        @Override
        public int worth() {
            return worth;
        }

        @Override
        public Predicate<Hunk> bindPredicate(final Lookups lookups) {
            return mockPredicate;
        }
    };

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockPredicate.test(any())).thenReturn(true);
    }

    @Test
    void bind() {
        final Preference preference = instance.bind(mockLookups);
        final int result = preference.evaluate(mockHunk);
        assertEquals(worth, result);
        verify(mockPredicate).test(mockHunk);
    }

}