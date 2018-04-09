package osd.considerations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.input.Section;
import osd.schedule.Hunk;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class UserPreferenceTest {

    private final int worth = 12;
    @Mock private Section mockSection;
    @Mock private Hunk mockHunk;

    // Our test instance always reports match results as matchResult -
    // this is OK, since getMatch() is inherited and not under test here.
    private UserConsideration.Match matchResult = null;
    private UserPreference instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new UserPreference(mockSection, mockSection, worth) {
            @Override
            Match getMatch(final Hunk hunk) {
                return Objects.requireNonNull(matchResult, "set matchResult before running tests");
            }
        };
    }

    @Test
    void worth() {
        assertEquals(worth, instance.worth());
    }

    @Test
    void test_MatchIsBoth() {
        matchResult = UserConsideration.Match.BOTH;
        assertTrue(0 != instance.evaluate(mockHunk));
    }

    @Test
    void test_MatchIsLeft() {
        matchResult = UserConsideration.Match.LEFT;
        assertTrue(0 == instance.evaluate(mockHunk));
    }

    @Test
    void test_MatchIsRight() {
        matchResult = UserConsideration.Match.RIGHT;
        assertTrue(0 == instance.evaluate(mockHunk));
    }

    @Test
    void test_MatchIsNeither() {
        matchResult = UserConsideration.Match.NEITHER;
        assertTrue(0 == instance.evaluate(mockHunk));
    }

    @Test
    void test_MatchIsNull() {
        matchResult = UserConsideration.Match.INCONCLUSIVE;
        assertTrue(0 == instance.evaluate(mockHunk));
    }

}