package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.Section;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PriorityTrackerTest {

    @Mock private Section mockSectionPriority1 = mock(Section.class);
    @Mock private Section mockSectionPriority2 = mock(Section.class);
    @Mock private Section mockSectionPriority3 = mock(Section.class);

    private final PriorityTracker instance = new PriorityTracker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getLowest_InitiallyNull() {
        assertNull(instance.getHighPrioritySections());
    }

    @Test
    void getLowest_AfterAddition() {
        instance.add(1L, mockSectionPriority1);
        assertEquals(Collections.singleton(mockSectionPriority1), instance.getHighPrioritySections());
    }

    @Test
    void getLowest_AfterMultipleAdditions() {
        instance.add(2L, mockSectionPriority2);
        instance.add(1L, mockSectionPriority1);
        instance.add(3L, mockSectionPriority3);
        assertEquals(Collections.singleton(mockSectionPriority1), instance.getHighPrioritySections());
    }

    @Test
    void getLowest_AfterRemoval() {
        instance.add(2L, mockSectionPriority2);
        instance.add(1L, mockSectionPriority1);
        instance.remove(1L);
        assertEquals(Collections.singleton(mockSectionPriority2), instance.getHighPrioritySections());
    }

    @Test
    void getLowest_AfterReversedRemoval() {
        instance.add(2L, mockSectionPriority2);
        instance.add(1L, mockSectionPriority1);
        instance.reversed().remove(mockSectionPriority1);
        assertEquals(Collections.singleton(mockSectionPriority2), instance.getHighPrioritySections());
    }

    @Test
    void getLowest_AfterRemoval2() {
        instance.add(2L, mockSectionPriority2);
        instance.add(1L, mockSectionPriority1);
        instance.remove(1L, mockSectionPriority1);
        assertEquals(Collections.singleton(mockSectionPriority2), instance.getHighPrioritySections());
    }

    @Test
    void copyConstructor_SameData() {
        instance.add(1L, mockSectionPriority1);
        instance.add(2L, mockSectionPriority2);
        instance.add(3L, mockSectionPriority3);
        final Set<Section> expected = instance.getHighPrioritySections();
        final PriorityTracker copy = new PriorityTracker(instance);
        final Set<Section> result = copy.getHighPrioritySections();
        assertEquals(expected, result);
    }

    @Test
    void copyConstructor_Distinct_FromOriginalToCopy() {
        instance.add(1L, mockSectionPriority1);
        instance.add(2L, mockSectionPriority2);
        instance.add(3L, mockSectionPriority3);
        final Set<Section> expected = instance.getHighPrioritySections();
        final PriorityTracker copy = new PriorityTracker(instance);
        instance.remove(1L);
        instance.remove(2L);
        instance.remove(3L);
        final Set<Section> result = copy.getHighPrioritySections();
        assertEquals(expected, result);
    }

    @Test
    void copyConstructor_Distinct_FromCopyToOriginal() {
        instance.add(1L, mockSectionPriority1);
        instance.add(2L, mockSectionPriority2);
        instance.add(3L, mockSectionPriority3);
        final Set<Section> expected = instance.getHighPrioritySections();
        final PriorityTracker copy = new PriorityTracker(instance);
        copy.remove(1L);
        copy.remove(2L);
        copy.remove(3L);
        final Set<Section> result = instance.getHighPrioritySections();
        assertEquals(expected, result);
    }

    @Test
    void ensurePresent() {
        assertThrows(NoSuchElementException.class,
                () -> instance.ensurePresent(mockSectionPriority1));
    }

    @Test
    void ensurePresent_NoSuperfluousExceptions() {
        instance.add(1L, mockSectionPriority1);
        instance.ensurePresent(mockSectionPriority1);
    }

    @Test
    void reversed() {
        instance.add(1L, mockSectionPriority1);
        assertEquals(1L, (long)instance.reversed().get(mockSectionPriority1));
    }

    @Test
    void doubleReversed() {
        assertTrue(instance.equals(instance.reversed().reversed()));
    }

}