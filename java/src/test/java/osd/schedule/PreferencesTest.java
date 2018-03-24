package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.considerations.BasePreference;
import osd.considerations.Lookups;
import osd.considerations.Preference;
import osd.output.Hunk;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PreferencesTest {

    @Mock private Preference mockPreference;
    @Mock private Hunk mockHunkGood, mockHunkBad;
    @Mock private Lookups mockLookups;

    @Mock private BasePreference mockBasePreference;
    @Mock private Preference mockBasePreferenceResult;

    private Preferences instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockPreference.evaluate(mockHunkGood)).thenReturn(1);
        when(mockPreference.evaluate(mockHunkBad)).thenReturn(-1);
        instance = new Preferences(Collections.singleton(mockPreference),
                                   Collections.singleton(mockBasePreference));
        when(mockBasePreference.bind(any())).thenReturn(mockBasePreferenceResult);
    }

    @Test
    void bind() {
        final int result = instance.bind(mockLookups).compare(mockHunkGood, mockHunkBad);
        assertTrue(result < 0);
    }

    @Test
    void bind_ReverseOrder() {
        final int result = instance.bind(mockLookups).compare(mockHunkBad, mockHunkGood);
        assertTrue(result > 0);
    }

    @Test
    void bind_Stable() {
        final int result = instance.bind(mockLookups).compare(mockHunkGood, mockHunkGood);
        assertEquals(0, result);
    }

    @Test
    void bind_ConsidersBasePreferences() {
        // To make sure we actually look at the bound preference's evaluation,
        // we'll make the preference our base preference binds to *like* the
        // "bad" preference. Then, as long as we sort the "bad" hunk *before*
        // the "good" one, we know we've actually looked at the bound
        // preference. This is probably overkill...
        when(mockBasePreferenceResult.evaluate(mockHunkBad)).thenReturn(999);
        final int result = instance.bind(mockLookups).compare(mockHunkGood, mockHunkBad);
        assertTrue(result > 0);
        verify(mockBasePreference, atLeastOnce()).bind(mockLookups);
        verify(mockBasePreferenceResult, atLeastOnce()).evaluate(mockHunkGood);
        verify(mockBasePreferenceResult, atLeastOnce()).evaluate(mockHunkBad);
    }

}