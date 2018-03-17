package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.input.Section;
import osd.input.Sources;
import osd.output.Hunk;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchedulerTest {

    @Mock private Sources mockSources;
    @Mock private Constraints mockConstraints;
    @Mock private Preferences mockPreferences;
    @Mock private Availability mockAvailability;

    @Mock private Section mockSection1;
    @Mock private Section mockSection2;
    @Mock private Section mockSection3;
    @Mock private Hunk mockHunk1;
    @Mock private Hunk mockHunk2;
    @Mock private Hunk mockHunk3;
    @Mock private Iterator<Hunk> mockIterator;

    @Mock private Stream<Hunk> mockStream1;
    @Mock private Stream<Hunk> mockStream2;
    @Mock private Stream<Hunk> mockStream3;

    private Scheduler instance;
    private boolean flipPriority = false;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockHunk1.getSection()).thenReturn(mockSection1);
        when(mockHunk2.getSection()).thenReturn(mockSection2);
        when(mockHunk3.getSection()).thenReturn(mockSection3);
        when(mockSources.getSections()).thenAnswer(a ->
                Stream.of(mockSection1, mockSection2, mockSection3));
        when(mockAvailability.getImpacted(mockHunk1)).thenAnswer(a ->
                Stream.of(mockSection2, mockSection3));
        initMockStream(mockStream1, 1);
        initMockStream(mockStream2, 2);
        initMockStream(mockStream3, 3);
        when(mockAvailability.candidates(mockSection1)).thenReturn(mockStream1);
        when(mockAvailability.candidates(mockSection2)).thenReturn(mockStream2);
        when(mockAvailability.candidates(mockSection3)).thenReturn(mockStream3);
        when(mockConstraints.test(any())).thenReturn(true);

        instance = new Scheduler(mockSources, mockConstraints, mockPreferences, mockAvailability);

        clearInvocations(mockAvailability); // Ignore candidates() calls from the constructor
    }

    @Test
    void getCandidateHunks() {
        final Iterator<Hunk> result = instance.getCandidateHunks(mockSection1).iterator();
        verify(mockAvailability).candidates(mockSection1);
        verify(mockStream1).sorted(mockPreferences);
        verify(mockStream1).iterator();
        assertEquals(mockIterator, result);
    }

    @Test
    void getNextSection() {
        assertEquals(mockSection1, instance.getNextSection());
    }

    @Test
    void addHunk() {
        flipPriority = true;
        assertTrue(instance.addHunk(mockHunk1));
        assertEquals(mockSection3, instance.getNextSection());
        flipPriority = false;
        assertTrue(instance.addHunk(mockHunk3));
        assertEquals(mockSection2, instance.getNextSection());
        assertTrue(instance.addHunk(mockHunk2));
        assertNull(instance.getNextSection());
        verify(mockAvailability).onHunkAdded(mockHunk1);
        verify(mockAvailability).onHunkAdded(mockHunk3);
        verify(mockAvailability).onHunkAdded(mockHunk2);
    }

    @Test
    void addHunk_ChecksConstraints() {
        when(mockConstraints.test(any())).thenReturn(false);
        assertFalse(instance.addHunk(mockHunk1));
        verify(mockAvailability, never()).onHunkAdded(any());
    }

    @Test
    void addHunk_FailsOnUnexpectedSection() {
        final Section unexpectedMockSection = mock(Section.class);
        final Hunk hunkWithUnexpectedSection = new Hunk(unexpectedMockSection, null, null, null);
        assertThrows(IllegalArgumentException.class, () -> instance.addHunk(hunkWithUnexpectedSection));
    }

    private void initMockStream(final Stream<Hunk> mockStream, final long priority) {
        when(mockStream.count()).thenAnswer(a -> priority * (flipPriority ? -1 : 1));
        when(mockStream.sorted(mockPreferences)).thenReturn(mockStream);
        when(mockStream.iterator()).thenReturn(mockIterator);
    }

}