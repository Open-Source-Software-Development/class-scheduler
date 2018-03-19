package osd.util.relation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ManyToManyTest {

    private final ManyToManyRelation<Character, String> instance
            = new ManyToManyRelation<>();
    private final String[] values = {"spam", "eggs", "foo", "bar"};

    @BeforeEach
    void setUp() {
        for (final String value: values) {
            for (final Character key: value.toCharArray()) {
                instance.add(key, value);
            }
        }
    }

    @Test
    void get() {
        final Set<String> expected = new HashSet<>(Arrays.asList("spam", "eggs"));
        final Set<String> result = instance.get('s');
        assertEquals(expected, result);
    }

    @Test
    void get_RespectsRemoval() {
        instance.remove('s', "spam");
        final Set<String> expected = Collections.singleton("eggs");
        final Set<String> result = instance.get('s');
        assertEquals(expected, result);
    }

    @Test
    void get_NullOnMissingKey() {
        assertNull(instance.get('z'));
    }

    @Test
    void get_NullAfterEverythingRemoved() {
        instance.remove('s');
        assertNull(instance.get('s'));
    }

    @Test
    void get_NullAfterEverythingRemoved_NonBatch() {
        instance.remove('s', "spam");
        instance.remove('s', "eggs");
        assertNull(instance.get('s'));
    }

}