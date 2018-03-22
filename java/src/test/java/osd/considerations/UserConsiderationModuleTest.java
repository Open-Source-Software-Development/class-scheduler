package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.UserConstraintFactory;
import osd.database.UserConstraintRecord;
import osd.database.UserPreferenceFactory;
import osd.database.UserPreferenceRecord;
import osd.input.Section;
import osd.output.Hunk;
import osd.util.ImmutablePair;
import osd.util.Pair;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserConsiderationModuleTest {

    @Mock private UserConstraintFactory mockUserConstraintFactory;
    @Mock private Section mockSectionA;
    @Mock private Hunk mockHunkA;
    @Mock private Section mockSectionB;

    @Mock private UserConstraintRecord mockRecordWhitelistA1;
    @Mock private UserConstraintRecord mockRecordWhitelistA2;
    @Mock private UserConstraintRecord mockRecordWhitelistB1;
    @Mock private UserConstraintRecord mockRecordWhitelistB2;
    @Mock private UserConstraintRecord mockRecordBlacklist1;
    @Mock private UserConstraintRecord mockRecordBlacklist2;

    @Mock private UserConstraint mockConstraintWhitelistA1;
    @Mock private UserConstraint mockConstraintWhitelistA2;
    @Mock private UserConstraint mockConstraintWhitelistB1;
    @Mock private UserConstraint mockConstraintWhitelistB2;
    @Mock private UserConstraint mockConstraintBlacklist1;
    @Mock private UserConstraint mockConstraintBlacklist2;

    private Collection<UserConstraintRecord> records;

    @Mock private UserPreferenceRecord mockUserPreferenceRecord;
    @Mock private UserPreference mockUserPreference;
    @Mock private UserPreferenceFactory mockUserPreferenceFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockUserConstraintFactory.apply(any())).thenCallRealMethod();

        when(mockConstraintWhitelistA1.or(any())).thenCallRealMethod();
        when(mockConstraintWhitelistA1.and(any())).thenCallRealMethod();
        when(mockConstraintWhitelistA1.test(any())).thenReturn(true);
        when(mockConstraintWhitelistA1.getWhitelistKey()).thenReturn(mockWhitelistKey(mockSectionA));
        when(mockUserConstraintFactory.create(mockRecordWhitelistA1)).thenReturn(mockConstraintWhitelistA1);

        when(mockConstraintWhitelistA2.or(any())).thenCallRealMethod();
        when(mockConstraintWhitelistA2.and(any())).thenCallRealMethod();
        when(mockConstraintWhitelistA2.test(any())).thenReturn(true);
        when(mockConstraintWhitelistA2.getWhitelistKey()).thenReturn(mockWhitelistKey(mockSectionA));
        when(mockUserConstraintFactory.create(mockRecordWhitelistA2)).thenReturn(mockConstraintWhitelistA2);

        when(mockConstraintWhitelistB1.or(any())).thenCallRealMethod();
        when(mockConstraintWhitelistB1.and(any())).thenCallRealMethod();
        when(mockConstraintWhitelistB1.test(any())).thenReturn(true);
        when(mockConstraintWhitelistB1.getWhitelistKey()).thenReturn(mockWhitelistKey(mockSectionB));
        when(mockUserConstraintFactory.create(mockRecordWhitelistB1)).thenReturn(mockConstraintWhitelistB1);

        when(mockConstraintWhitelistB2.or(any())).thenCallRealMethod();
        when(mockConstraintWhitelistB2.and(any())).thenCallRealMethod();
        when(mockConstraintWhitelistB2.test(any())).thenReturn(true);
        when(mockConstraintWhitelistB2.getWhitelistKey()).thenReturn(mockWhitelistKey(mockSectionB));
        when(mockUserConstraintFactory.create(mockRecordWhitelistB2)).thenReturn(mockConstraintWhitelistB2);

        when(mockConstraintBlacklist1.or(any())).thenCallRealMethod();
        when(mockConstraintBlacklist1.and(any())).thenCallRealMethod();
        when(mockConstraintBlacklist1.test(any())).thenReturn(true);
        when(mockConstraintBlacklist1.getWhitelistKey()).thenReturn(ImmutablePair.of(null, null));
        when(mockUserConstraintFactory.create(mockRecordBlacklist1)).thenReturn(mockConstraintBlacklist1);

        when(mockConstraintBlacklist2.or(any())).thenCallRealMethod();
        when(mockConstraintBlacklist2.and(any())).thenCallRealMethod();
        when(mockConstraintBlacklist2.test(any())).thenReturn(true);
        when(mockConstraintBlacklist2.getWhitelistKey()).thenReturn(ImmutablePair.of(null, null));
        when(mockUserConstraintFactory.create(mockRecordBlacklist2)).thenReturn(mockConstraintBlacklist2);

        records = Arrays.asList(
                mockRecordWhitelistA1,
                mockRecordWhitelistA2,
                mockRecordWhitelistB1,
                mockRecordWhitelistB2,
                mockRecordBlacklist1,
                mockRecordBlacklist2);
        when(mockHunkA.getSection()).thenReturn(mockSectionA);

        when(mockUserPreferenceFactory.create(mockUserPreferenceRecord)).thenReturn(mockUserPreference);
        when(mockUserPreferenceFactory.apply(any())).thenCallRealMethod();
    }

    @Test
    void providesUserPreferences() {
        final Set<Preference> expected = Collections.singleton(mockUserPreference);
        final Set<Preference> result = new HashSet<>(
                UserConsiderationModule.providesUserPreferences(
                        Collections.singleton(mockUserPreferenceRecord),
                        mockUserPreferenceFactory
                )
        );
        assertEquals(expected, result);
    }

    @Test
    void providesUserConstraints_EverythingPasses() {
        final boolean result =
                UserConsiderationModule.providesUserConstraints(records, mockUserConstraintFactory)
                        .stream().allMatch(c -> c.test(mockHunkA));
        assertTrue(result);
        verify(mockConstraintBlacklist1).test(mockHunkA);
        verify(mockConstraintBlacklist2).test(mockHunkA);
    }

    @Test
    void providesUserConstraints_OnlyOneWhitelistNeeded() {
        when(mockConstraintWhitelistA1.test(any())).thenReturn(false);
        final boolean result =
                UserConsiderationModule.providesUserConstraints(records, mockUserConstraintFactory)
                        .stream().allMatch(c -> c.test(mockHunkA));
        assertTrue(result);
        verify(mockConstraintBlacklist1).test(mockHunkA);
        verify(mockConstraintBlacklist2).test(mockHunkA);
        verify(mockConstraintWhitelistA1).test(mockHunkA);
        verify(mockConstraintWhitelistA2).test(mockHunkA);
    }

    @Test
    void providesUserConstraints_AtLeastOneWhitelistNeeded() {
        when(mockConstraintWhitelistA1.test(any())).thenReturn(false);
        when(mockConstraintWhitelistA2.test(any())).thenReturn(false);
        final boolean result =
                UserConsiderationModule.providesUserConstraints(records, mockUserConstraintFactory)
                        .stream().allMatch(c -> c.test(mockHunkA));
        assertFalse(result);
    }

    @Test
    void providesUserConstraints_AllBlacklistsNeeded() {
        when(mockConstraintBlacklist1.test(any())).thenReturn(false);
        final boolean result =
                UserConsiderationModule.providesUserConstraints(records, mockUserConstraintFactory)
                        .stream().allMatch(c -> c.test(mockHunkA));
        assertFalse(result);
    }

    private static Pair<Object, HunkField<?>> mockWhitelistKey(final Section section) {
        return ImmutablePair.of(section, HunkField.get(section));
    }

}