package osd.util.relation;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ManyToManyTest {

    private final ManyToManyRelation<Integer, String> instance = new ManyToManyRelation<>();

    @Test
    void get_NullOnMissingKey() {
        assertNull(instance.get(7));
    }

    @Test
    void add() {
        instance.add(3, "foo");
        final Set<String> expected = Collections.singleton("foo");
        final Set<String> result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void add_Multiple() {
        instance.add(3, "foo");
        instance.add(3, "bar");
        final Set<String> expected = new HashSet<>(Arrays.asList("foo", "bar"));
        final Set<String> result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void remove() {
        instance.add(4, "spam");
        instance.add(4, "eggs");
        instance.remove(4, "spam");
        final Set<String> expected = Collections.singleton("eggs");
        final Set<String> result = instance.get(4);
        assertEquals(expected, result);
    }

    @Test
    void remove_Batch() {
        instance.add(4, "spam");
        instance.add(4, "eggs");
        instance.remove(4);
        assertNull(instance.get(4));
    }

    @Test
    void get_NullAfterEverythingRemoved_NonBatch() {
        instance.add(4, "spam");
        instance.add(4, "eggs");
        instance.remove(4, "spam");
        instance.remove(4, "eggs");
        assertNull(instance.get(4));
    }

    @Test
    void copyConstructor_SameData() {
        RelationTest.testCopyConstructor_SameData(instance, ManyToManyRelation::new, 3, "foo");
    }

    @Test
    void copyConstructor_Separate() {
        RelationTest.testCopyConstructor_Separate(instance, ManyToManyRelation::new, 3, "foo");
    }

    @Test
    void viewConstructor_ReadsThrough() {
        final Map<Integer, Set<String>> forward = new HashMap<>();
        final Map<String, Set<Integer>> reverse = new HashMap<>();
        forward.computeIfAbsent(3, i -> new HashSet<>()).add("foo");
        reverse.computeIfAbsent("foo", i -> new HashSet<>()).add(3);
        final ManyToManyRelation<Integer, String> view
                = new ManyToManyRelation<>(forward, reverse);
        final Set<String> expected = Collections.singleton("foo");
        final Set<String> result = view.get(3);
        assertEquals(expected, result);
    }

    @Test
    void viewConstructor_WritesThrough() {
        final Map<Integer, Set<String>> forward = new HashMap<>();
        final Map<String, Set<Integer>> reverse = new HashMap<>();
        final ManyToManyRelation<Integer, String> view
                = new ManyToManyRelation<>(forward, reverse);
        view.add(3, "foo");
        final Set<String> expected = Collections.singleton("foo");
        final Set<String> result = forward.get(3);
        assertEquals(expected, result);
    }

    @Test
    void reversed_ReadsThrough() {
        instance.add(3, "foo");
        final ManyToManyRelation<String, Integer> reversed = instance.reversed();
        final Set<Integer> expected = Collections.singleton(3);
        final Set<Integer> result = reversed.get("foo");
        assertEquals(expected, result);
    }

    @Test
    void reversed_WritesThrough() {
        final ManyToManyRelation<String, Integer> reversed = instance.reversed();
        reversed.add("foo", 3);
        final Set<String> expected = Collections.singleton("foo");
        final Set<String> result = instance.get(3);
        assertEquals(expected, result);
    }

}