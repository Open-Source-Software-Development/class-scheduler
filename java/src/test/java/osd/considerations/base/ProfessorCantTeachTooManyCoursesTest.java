package osd.considerations.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.considerations.Lookups;
import osd.input.Professor;
import osd.output.Hunk;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProfessorCantTeachTooManyCoursesTest {

    // A mock professor who can teach four classes.
    @Mock private Professor mockProfessor;
    private final int capacity = 4;

    // A mock lookups instance that reports our professor
    // as teaching currentCourseLoad courses.
    @Mock private Lookups mockLookups;
    @Mock private Stream<Hunk> mockStream;
    private long currentCourseLoad;

    @Mock private Hunk mockHunk;
    private ProfessorCantTeachTooManyCourses instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockProfessor.getCourseCapacity()).thenReturn(capacity);
        when(mockHunk.getProfessor()).thenReturn(mockProfessor);

        when(mockLookups.lookup(mockProfessor)).thenReturn(mockStream);
        when(mockStream.count()).then(a -> currentCourseLoad);

        instance = new ProfessorCantTeachTooManyCourses();
    }

    @Test
    void bind_PermitsWhenBelowCapacity() {
        currentCourseLoad = capacity - 1;
        final boolean expected = true;
        final boolean result = instance.bind(mockLookups).test(mockHunk);
        assertEquals(expected, result);
    }

    @Test
    void bind_RejectsWhenAtCapacity() {
        currentCourseLoad = capacity;
        final boolean expected = false;
        final boolean result = instance.bind(mockLookups).test(mockHunk);
        assertEquals(expected, result);
    }

    @Test
    void bind_RejectsWhenAboveCapacity() {
        currentCourseLoad = capacity + 1;
        final boolean expected = false;
        final boolean result = instance.bind(mockLookups).test(mockHunk);
        assertEquals(expected, result);
    }

}