package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.input.Section;

import java.util.Collections;

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
        assertNull(instance.getHighPriority());
    }

    @Test
    void getLowest_AfterAddition() {
        instance.add(1L, mockSectionPriority1);
        assertEquals(Collections.singleton(mockSectionPriority1), instance.getHighPriority());
    }

    @Test
    void getLowest_AfterMultipleAdditions() {
        instance.add(2L, mockSectionPriority2);
        instance.add(1L, mockSectionPriority1);
        instance.add(3L, mockSectionPriority3);
        assertEquals(Collections.singleton(mockSectionPriority1), instance.getHighPriority());
    }

    @Test
    void getLowest_AfterRemoval() {
        instance.add(2L, mockSectionPriority2);
        instance.add(1L, mockSectionPriority1);
        instance.remove(1L);
        assertEquals(Collections.singleton(mockSectionPriority2), instance.getHighPriority());
    }


    @Test
    void getLowest_AfterReversedRemoval() {
        instance.add(2L, mockSectionPriority2);
        instance.add(1L, mockSectionPriority1);
        instance.reversed().remove(mockSectionPriority1);
        assertEquals(Collections.singleton(mockSectionPriority2), instance.getHighPriority());
    }

}