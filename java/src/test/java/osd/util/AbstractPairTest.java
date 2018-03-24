package osd.util;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AbstractPairTest {

    private final Object LEFT_OBJECT = new Object();
    private final Object RIGHT_OBJECT = new Object();
    private final AbstractPair<Object, Object> instance = new AbstractPair<Object, Object>() {
        @Override
        public Object left() {
            return LEFT_OBJECT;
        }

        @Override
        public Object right() {
            return RIGHT_OBJECT;
        }

        @Override
        public ImmutablePair<Object, Object> immutable() {
            throw new UnsupportedOperationException("not implemented on test instance");
        }
    };

    @Test
    void equals_True() {
        @SuppressWarnings("unchecked")
        final Pair<Object, Object> shouldBeEqual = mock(Pair.class);
        when(shouldBeEqual.left()).thenReturn(LEFT_OBJECT);
        when(shouldBeEqual.right()).thenReturn(RIGHT_OBJECT);
        assertTrue(instance.equals(shouldBeEqual));
    }

    @Test
    void equals_False() {
        final Object anotherObject = new Object();
        @SuppressWarnings("unchecked")
        final Pair<Object, Object> shouldNotBeEqual = mock(Pair.class);
        when(shouldNotBeEqual.left()).thenReturn(LEFT_OBJECT);
        when(shouldNotBeEqual.right()).thenReturn(anotherObject);
        assertFalse(instance.equals(shouldNotBeEqual));
    }

    @Test
    void testHashCode() {
        final int expected = Objects.hash(LEFT_OBJECT, RIGHT_OBJECT);
        final int result = instance.hashCode();
        assertEquals(expected, result);
    }

    @Test
    void testToString() {
        final String expected = "(" + LEFT_OBJECT + ", " + RIGHT_OBJECT + ")";
        final String result = instance.toString();
        assertEquals(expected, result);
    }

}