package osd.util.relation;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OneToManyTest {

    private final OneToManyRelation<Integer, String> instance
            = new OneToManyRelation<>();

    @Test
    void add() {
        instance.add(3, "foo");
        instance.add(3, "bar");
        final Set<String> expected = new HashSet<>(Arrays.asList("foo", "bar"));
        final Set<String> result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void add_CleansUpPreviousAssociation() {
        instance.add(3, "foo");
        instance.add(4, "foo"); // Reassigning the same value to a different key.
        instance.add(3, "bar");
        final Set<String> expected = Collections.singleton("bar");
        final Set<String> result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void add_CleansUpPreviousAssociation_ToNullOnceEmpty() {
        instance.add(3, "foo");
        instance.add(4, "foo"); // Reassigning the same value to a different key.
        final Set<String> expected = null;
        final Set<String> result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void remove() {
        instance.add(3, "foo");
        instance.remove(3, "foo"); // Remove the value we just assigned.
        instance.add(3, "bar");
        final Set<String> expected = Collections.singleton("bar");
        final Set<String> result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void remove_NullifiesOnceEmpty() {
        instance.add(3, "foo");
        instance.remove(3); // Remove the value we just assigned.
        final Set<String> expected = null;
        final Set<String> result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void removeBatch() {
        instance.add(3, "foo");
        instance.add(3, "bar");
        instance.remove(3);
        final Set<String> expected = null;
        final Set<String> result = instance.get(3);
        assertEquals(expected, result);
    }

    @Test
    void copyConstructor_SameData() {
        RelationTest.testCopyConstructor_SameData(instance, OneToManyRelation::new, 3, "foo");
    }

    @Test
    void copyConstructor_Separate() {
        RelationTest.testCopyConstructor_Separate(instance, OneToManyRelation::new, 3, "foo");
    }

    @Test
    void viewConstructor_ReadsThrough() {
        final Map<Integer, Set<String>> forward = new HashMap<>();
        final Map<String, Integer> reverse = new HashMap<>();
        forward.computeIfAbsent(3, i -> new HashSet<>()).add("foo");
        reverse.put("foo", 3);
        final OneToManyRelation<Integer, String> view
                = new OneToManyRelation<>(forward, reverse);
        final Set<String> expected = Collections.singleton("foo");
        final Set<String> result = view.get(3);
        assertEquals(expected, result);
    }

    @Test
    void viewConstructor_WritesThrough() {
        final Map<Integer, Set<String>> forward = new HashMap<>();
        final Map<String, Integer> reverse = new HashMap<>();
        final OneToManyRelation<Integer, String> view
                = new OneToManyRelation<>(forward, reverse);
        view.add(3, "foo");
        final Set<String> expected = Collections.singleton("foo");
        final Set<String> result = forward.get(3);
        assertEquals(expected, result);
    }

    @Test
    void reversed_ReadsThrough() {
        instance.add(3, "foo");
        final ManyToOneRelation<String, Integer> reversed = instance.reversed();
        final Integer expected = 3;
        final Integer result = reversed.get("foo");
        assertEquals(expected, result);
    }

    @Test
    void reversed_WritesThrough() {
        final ManyToOneRelation<String, Integer> reversed = instance.reversed();
        reversed.add("foo", 3);
        final Set<String> expected = Collections.singleton("foo");
        final Set<String> result = instance.get(3);
        assertEquals(expected, result);
    }

}