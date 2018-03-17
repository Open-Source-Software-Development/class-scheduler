package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.considerations.Preference;
import osd.output.Hunk;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PreferencesTest {

    @Mock private Preference mockPreference;
    @Mock private Hunk mockHunkGood, mockHunkBad;
    private Preferences instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockPreference.evaluate(mockHunkGood)).thenReturn(1);
        when(mockPreference.evaluate(mockHunkBad)).thenReturn(-1);
        instance = new Preferences(Collections.singleton(mockPreference));
    }

    @Test
    void compare() {
        final int result = instance.compare(mockHunkGood, mockHunkBad);
        assertTrue(result < 0);
    }

    @Test
    void compare_ReverseOrder() {
        final int result = instance.compare(mockHunkBad, mockHunkGood);
        assertTrue(result > 0);
    }

    @Test
    void compare_Stable() {
        final int result = instance.compare(mockHunkGood, mockHunkGood);
        assertEquals(0, result);
    }

}