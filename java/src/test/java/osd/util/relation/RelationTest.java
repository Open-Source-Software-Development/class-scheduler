package osd.util.relation;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

class RelationTest {

    private static final class RelationTestImpl extends Relation<Integer, Integer, String, String> {

        RelationTestImpl() {}

        RelationTestImpl(final RelationTestImpl copyOf) {
            super(copyOf);
        }

        RelationTestImpl(final Map<Integer, String> forward, final Map<String, Integer> reverse) {
            super(forward, reverse);
        }

        @Override
        public void add(final Integer key, final String value) {
            forward.put(key, value);
            reverse.put(value, key);
        }

        @Override
        public void remove(final Integer key, final String value) {
            forward.remove(key, value);
            reverse.remove(value, key);
        }

        @Override
        public void remove(final Integer key) {
            final String value = forward.remove(key);
            if (value != null) {
                reverse.remove(value);
            }
        }

        @Override
        public Relation<String, String, Integer, Integer> reversed() {
            throw new UnsupportedOperationException("I didn't bother writing this for the test implementation");
        }
    }

    private final RelationTestImpl instance = new RelationTestImpl();

    @Test
    void isEmpty_InitiallyTrue() {
        assertTrue(instance.isEmpty());
    }

    @Test
    void isEmpty_FalseAfterAddition() {
        instance.add(3, "foo");
        assertFalse(instance.isEmpty());
    }

    @Test
    void getOrDefault_GetWhenPresent() {
        instance.add(3, "foo");
        assertEquals("foo", instance.getOrDefault(3, () -> "bar"));
    }

    @Test
    void getOrDefault_ConsiderSupplierWhenAbsent() {
        assertEquals("bar", instance.getOrDefault(3, () -> "bar"));
    }

    @Test
    void testHashCode() {
        instance.add(3, "foo");
        final int expected = instance.forward.hashCode();
        final int result = instance.hashCode();
        assertEquals(expected, result);
    }

    @Test
    void equals_TrueWhenBothEmpty() {
        final RelationTestImpl other = new RelationTestImpl();
        assertTrue(instance.equals(other));
    }

    @Test
    void equals_TrueAfterSameInserts() {
        instance.add(3, "foo");
        final RelationTestImpl other = new RelationTestImpl();
        other.add(3, "foo");
        assertTrue(instance.equals(other));
    }

    @Test
    void equals_FalseAfterDifferentInserts() {
        instance.add(3, "foo");
        final RelationTestImpl other = new RelationTestImpl();
        assertFalse(instance.equals(other));
    }

    @Test
    void testToString() {
        final String expected = instance.forward.toString();
        final String result = instance.toString();
        assertEquals(expected, result);
    }

    @Test
    void copyConstructor_SameData() {
        testCopyConstructor_SameData(instance, RelationTestImpl::new, 3, "foo");
    }

    @Test
    void copyConstructor_Separate() {
        testCopyConstructor_Separate(instance, RelationTestImpl::new, 3, "foo");
    }

    @Test
    void viewConstructor() {
        final RelationTestImpl other = new RelationTestImpl(instance.forward, instance.reverse);
        other.add(3, "foo");
        assertEquals("foo", instance.get(3));
    }

    static <K, V, T extends Relation<K, ?, V, ?>>
    void testCopyConstructor_SameData(final T instance, UnaryOperator<T> ctor, final K key, final V value) {
        instance.add(key, value);
        final T copy = ctor.apply(instance);
        assertEquals(instance.get(key), copy.get(key));
    }

    static <K, V, T extends Relation<K, ?, V, ?>>
    void testCopyConstructor_Separate(final T instance, UnaryOperator<T> ctor, final K key, final V value) {
        final Object expected = instance.get(key);
        final T copy = ctor.apply(instance);
        instance.add(key, value);
        assertEquals(expected, copy.get(key));
    }

}