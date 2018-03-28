package osd.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImmutablePairTest {

    private final int SOME_INT = 3;
    private final String SOME_STRING = "foo";
    private final ImmutablePair<Integer, String> instance = ImmutablePair.of(SOME_INT, SOME_STRING);

    @Test
    void immutable() {
        assertTrue(instance == instance.immutable());
    }

    @Test
    void of() {
        assertEquals(SOME_INT, (int)instance.left());
        assertEquals(SOME_STRING, instance.right());
    }

}