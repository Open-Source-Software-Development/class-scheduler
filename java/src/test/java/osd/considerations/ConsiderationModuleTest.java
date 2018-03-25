package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.Course;
import osd.database.Professor;
import osd.database.Room;
import osd.database.Section;
import osd.schedule.Hunk;
import osd.schedule.Lookups;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unused")
class ConsiderationModuleTest {

    @Mock private Section mockSection;
    @Mock private Hunk mockHunk;
    @Mock private Lookups mockLookups;

    @Mock private UserPreference mockUserPreference;
    @Mock private BasePreference mockBasePreference;
    // Returned when mockBasePreference is bound.
    @Mock private Preference mockBoundPreference;

    @Mock private UserConstraint mockUserConstraint;
    @Mock private BaseConstraint mockBaseConstraint;
    // Returned when mockBaseConstraint is bound.
    @Mock private Constraint mockBoundConstraint;

    private Course[] mockCourses;
    @Mock private Course mockCourse, anotherMockCourse;
    private Object[] mockRightValues;
    @Mock private Professor mockProfessorGood1, mockProfessorGood2, mockProfessorBad;
    @Mock private Room mockRoomGood, mockRoomBad, mockRoomBlacklisted;

    @MockConstraintData(course = 0, right = 0, isBlacklist = false)
    @Mock private UserConstraint mockUserConstraint_Course_GoodProfessor1_Whitelist;

    @MockConstraintData(course = 0, right = 1, isBlacklist = false)
    @Mock private UserConstraint mockUserConstraint_Course_GoodProfessor2_Whitelist;

    @MockConstraintData(course = 0, right = 3, isBlacklist = false)
    @Mock private UserConstraint mockUserConstraint_Course_GoodRoom_Whitelist;

    @MockConstraintData(course = 0, right = 5, isBlacklist = true)
    @Mock private UserConstraint mockUserConstraint_Course_BlacklistRoom_Blacklist;

    // "anotherMockCourse" exists specifically to allow for false positives.
    @MockConstraintData(course = 1, right = 2, isBlacklist = false)
    @Mock private UserConstraint mockUserConstraint_DistractionCourse_BadProfessor_Whitelist;
    @MockConstraintData(course = 1, right = 4, isBlacklist = false)
    @Mock private UserConstraint mockUserConstraint_DistractionCourse_BadRoom_Whitelist;

    private List<UserConstraint> userConstraints = new ArrayList<>();

    @BeforeEach
    void setUp() throws IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        when(mockBasePreference.bind(mockLookups)).thenReturn(mockBoundPreference);
        when(mockBaseConstraint.bind(mockLookups)).thenReturn(mockBoundConstraint);
        when(mockHunk.getSection()).thenReturn(mockSection);
        mockCourses = new Course[] {mockCourse, anotherMockCourse};
        mockRightValues = new Object[] {
                mockProfessorGood1, mockProfessorGood2, mockProfessorBad,
                mockRoomGood, mockRoomBad, mockRoomBlacklisted};
        mockConstraintsHelper();
    }

    @Test
    void providesPreferences() {
        when(mockUserPreference.evaluate(mockHunk)).thenReturn(1);
        when(mockBoundPreference.evaluate(mockHunk)).thenReturn(2);
        final BiFunction<Lookups, Hunk, Integer> result = ConsiderationModule.providesPreferences(
                Collections.singleton(mockUserPreference),
                Collections.singleton(mockBasePreference)
        );
        assertEquals(3, (int) result.apply(mockLookups, mockHunk));
        verify(mockBasePreference).bind(mockLookups);
        verify(mockUserPreference).evaluate(mockHunk);
        verify(mockBoundPreference).evaluate(mockHunk);
    }

    @Test
    void providesUserConstraints_Good() {
        final Predicate<Hunk> result = ConsiderationModule.providesUserConstraints(userConstraints);
        when(mockSection.getCourse()).thenReturn(mockCourse);
        when(mockHunk.getProfessor()).thenReturn(mockProfessorGood1);
        when(mockHunk.getRoom()).thenReturn(mockRoomGood);
        assertTrue(result.test(mockHunk));
    }

    @Test
    void providesUserConstraints_Whitelist() {
        final Predicate<Hunk> result = ConsiderationModule.providesUserConstraints(userConstraints);
        when(mockSection.getCourse()).thenReturn(mockCourse);
        when(mockHunk.getProfessor()).thenReturn(mockProfessorBad);
        when(mockHunk.getRoom()).thenReturn(mockRoomGood);
        assertFalse(result.test(mockHunk));
    }

    @Test
    void providesUserConstraints_Blacklist() {
        final Predicate<Hunk> result = ConsiderationModule.providesUserConstraints(userConstraints);
        when(mockSection.getCourse()).thenReturn(mockCourse);
        when(mockHunk.getProfessor()).thenReturn(mockProfessorGood1);
        when(mockHunk.getRoom()).thenReturn(mockRoomBlacklisted);
        assertFalse(result.test(mockHunk));
    }

    @Test
    void providesBaseConstraints() {
        providesBaseConstraintsHelper(true);
        providesBaseConstraintsHelper(false);
    }

    private void providesBaseConstraintsHelper(final boolean testReturns) {
        clearInvocations(mockBaseConstraint);
        clearInvocations(mockBoundConstraint);
        when(mockBoundConstraint.test(mockHunk)).thenReturn(testReturns);
        final BiPredicate<Lookups, Hunk> result = ConsiderationModule.providesBaseConstraints(
                Collections.singleton(mockBaseConstraint)
        );
        assertEquals(testReturns, result.test(mockLookups, mockHunk));
        verify(mockBaseConstraint).bind(mockLookups);
        verify(mockBoundConstraint).test(mockHunk);
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MockConstraintData {
        int course();
        int right();
        boolean isBlacklist();
    }

    private void mockConstraintsHelper() throws IllegalAccessException {
        for (final Field field: getClass().getDeclaredFields()) {
            final Object object = field.get(this);
            if (object instanceof UserConstraint) {
                final UserConstraint constraint = (UserConstraint)object;
                when(constraint.and(any())).thenCallRealMethod();
                when(constraint.or(any())).thenCallRealMethod();
                if (field.isAnnotationPresent(MockConstraintData.class)) {
                    final MockConstraintData data = field.getAnnotation(MockConstraintData.class);
                    final Object left = mockCourses[data.course()];
                    final Object right = mockRightValues[data.right()];
                    final UserConstraint implementation = new UserConstraint(left, right, data.isBlacklist());
                    when(constraint.getWhitelistKey()).thenReturn(implementation.getWhitelistKey());
                    when(constraint.test(any())).then(invocation -> implementation.test(invocation.getArgument(0)));
                    userConstraints.add(constraint);
                }
            }
        }
    }

}