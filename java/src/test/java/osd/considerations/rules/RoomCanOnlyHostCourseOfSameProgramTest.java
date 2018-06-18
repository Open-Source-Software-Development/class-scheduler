package osd.considerations.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.input.Course;
import osd.database.input.Room;
import osd.database.input.Section;
import osd.schedule.Hunk;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class RoomCanOnlyHostCourseOfSameProgramTest {

    @Mock private Room mockRoom;
    @Mock private Course mockCourse;
    @Mock private Section mockSection;
    private Hunk hunk;

    private RoomCanOnlyHostCourseOfSameProgram instance;

    private static final String SOME_PROGRAM = "Underwater Basketweaving";
    private static final String ANOTHER_PROGRAM = "Applied Theology";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockSection.getCourse()).thenReturn(mockCourse);
        when(mockRoom.getSubject()).thenReturn(SOME_PROGRAM);
        hunk = new Hunk(mockSection, null, mockRoom, null);
        instance = new RoomCanOnlyHostCourseOfSameProgram();
    }

    @Test
    void bind() {
        when(mockCourse.getProgram()).thenReturn(SOME_PROGRAM);
        final boolean result = instance.bind(null).test(hunk);
        assertTrue(result);
    }

    @Test
    void bind_FalseWhenDifferent() {
        when(mockCourse.getProgram()).thenReturn(ANOTHER_PROGRAM);
        final boolean result = instance.bind(null).test(hunk);
        assertFalse(result);
    }

}