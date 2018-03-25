package osd.util.relation;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManyToOneTest {

    private final ManyToOneRelation<Integer, String> instance
            = new ManyToOneRelation<>();

    @Test
    void add() {
        instance.add(3, "foo");
        final String expected = "foo";
        final String result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void add_CleansUpPreviousAssociation() {
        instance.add(3, "foo");
        instance.add(3, "bar");
        final String expected = "bar";
        final String result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void add_PermitsMultipleKeys() {
        instance.add(3, "foo");
        instance.add(4, "foo");
        final String expected = "foo";
        final String result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void remove() {
        instance.add(3, "foo");
        instance.remove(3, "foo"); // Remove the value we just assigned.
        final String expected = null;
        final String result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void removeBatch() {
        instance.add(3, "foo");
        instance.remove(3);
        final String expected = null;
        final String result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void copyConstructor_SameData() {
        RelationTest.testCopyConstructor_SameData(instance, ManyToOneRelation::new, 3, "foo");
    }

    @Test
    void copyConstructor_Separate() {
        RelationTest.testCopyConstructor_Separate(instance, ManyToOneRelation::new, 3, "foo");
    }

    @Test
    void viewConstructor_ReadsThrough() {
        final Map<Integer, String> forward = new HashMap<>();
        final Map<String, Set<Integer>> reverse = new HashMap<>();
        forward.put(3, "foo");
        reverse.computeIfAbsent("foo", z -> new HashSet<>()).add(3);
        final ManyToOneRelation<Integer, String> view
                = new ManyToOneRelation<>(forward, reverse);
        final String expected = "foo";
        final String result = view.get(3);
        assertEquals(expected, result);
    }

    @Test
    void viewConstructor_WritesThrough() {
        final Map<Integer, String> forward = new HashMap<>();
        final Map<String, Set<Integer>> reverse = new HashMap<>();
        final ManyToOneRelation<Integer, String> view
                = new ManyToOneRelation<>(forward, reverse);
        view.add(3, "foo");
        final String expected = "foo";
        final String result = forward.get(3);
        assertEquals(expected, result);
    }

    @Test
    void reversed_ReadsThrough() {
        instance.add(3, "foo");
        final OneToManyRelation<String, Integer> reversed = instance.reversed();
        final Set<Integer> expected = Collections.singleton(3);
        final Set<Integer> result = reversed.get("foo");
        assertEquals(expected, result);
    }

    @Test
    void reversed_WritesThrough() {
        final OneToManyRelation<String, Integer> reversed = instance.reversed();
        reversed.add("foo", 3);
        final String expected = "foo";
        final String result = instance.get(3);
        assertEquals(expected, result);
    }

}