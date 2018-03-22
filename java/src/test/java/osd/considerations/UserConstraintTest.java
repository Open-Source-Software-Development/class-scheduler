package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.input.Professor;
import osd.input.Section;
import osd.output.Hunk;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserConstraintTest {

    @Mock private Section mockSection;
    @Mock private Section anotherMockSection;
    @Mock private Professor mockProfessor;
    @Mock private Professor anotherMockProfessor;
    @Mock private Hunk mockHunk;

    private UserConstraint whitelist;
    private UserConstraint blacklist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        whitelist = new UserConstraint(mockSection, mockProfessor, false);
        blacklist = new UserConstraint(mockSection, mockProfessor, true);
    }

    @Test
    void test_TrueOnInconclusive_Whitelist() {
        when(mockHunk.getSection()).thenReturn(null);
        when(mockHunk.getProfessor()).thenReturn(null);
        assertTrue(whitelist.test(mockHunk));
    }

    @Test
    void test_TrueOnNeither_Whitelist() {
        when(mockHunk.getSection()).thenReturn(anotherMockSection);
        when(mockHunk.getProfessor()).thenReturn(anotherMockProfessor);
        assertTrue(whitelist.test(mockHunk));
    }

    @Test
    void test_TrueOnInconclusive_Blacklist() {
        when(mockHunk.getSection()).thenReturn(null);
        when(mockHunk.getProfessor()).thenReturn(null);
        assertTrue(blacklist.test(mockHunk));
    }

    @Test
    void test_TrueOnNeither_Blacklist() {
        when(mockHunk.getSection()).thenReturn(anotherMockSection);
        when(mockHunk.getProfessor()).thenReturn(anotherMockProfessor);
        assertTrue(blacklist.test(mockHunk));
    }

    @Test
    void test_TrueOnBothIfWhitelist() {
        when(mockHunk.getSection()).thenReturn(mockSection);
        when(mockHunk.getProfessor()).thenReturn(mockProfessor);
        assertTrue(whitelist.test(mockHunk));
    }

    @Test
    void test_FalseOnOneIfWhitelist() {
        when(mockHunk.getSection()).thenReturn(mockSection);
        when(mockHunk.getProfessor()).thenReturn(anotherMockProfessor);
        assertFalse(whitelist.test(mockHunk));
    }

    @Test
    void test_TrueOnOneIfBlacklist() {
        when(mockHunk.getSection()).thenReturn(mockSection);
        when(mockHunk.getProfessor()).thenReturn(anotherMockProfessor);
        assertTrue(blacklist.test(mockHunk));
    }

    @Test
    void test_FalseOnBothIfBlacklist() {
        when(mockHunk.getSection()).thenReturn(mockSection);
        when(mockHunk.getProfessor()).thenReturn(mockProfessor);
        assertFalse(blacklist.test(mockHunk));
    }

    @Test
    void getWhitelistKey() {
        assertEquals(mockSection, whitelist.getWhitelistKey());
    }

    @Test
    void getWhitelistKey_NullOnBlacklist() {
        assertNull(blacklist.getWhitelistKey());
    }

    @Test
    void isBlacklist_True() {
        assertFalse(whitelist.isBlacklist());
    }

    @Test
    void isBlacklist_False() {
        assertTrue(blacklist.isBlacklist());
    }

}