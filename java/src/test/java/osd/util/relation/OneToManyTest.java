package osd.util.relation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OneToManyTest {

    private final OneToManyRelation<Integer, String> instance = new OneToManyRelation<>();
    private final String[] values = {"spam", "eggs", "foo", "bar"};

    @BeforeEach
    void setUp() {
        for (final String value: values) {
            instance.add(value.length(), value);
        }
    }

    @Test
    void get() {
        final Set<String> expected = new HashSet<>(Arrays.asList("spam", "eggs"));
        final Set<String> result = instance.get(4);
        assertEquals(expected, result);
    }

    @Test
    void getReversed() {
        final int expected = 3;
        final int result = instance.reversed().get("bar");
        assertEquals(expected, result);
    }

    @Test
    void getReversed_UpdatesProperly() {
        final int expected = -12;
        instance.add(expected, "eggs");
        final int result = instance.reversed().get("eggs");
        assertEquals(expected, result);
    }

    @Test
    void get_UpdatesProperly() {
        instance.add(-12, "eggs");
        final Set<String> expected = Collections.singleton("spam");
        final Set<String> result = instance.get(4);
        assertEquals(expected, result);
    }

}