package osd.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    private static final class PairTestImpl implements Pair<Object, Object> {

        private final Object left, right;

        PairTestImpl(final Object left, final Object right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public Object left() {
            return left;
        }

        @Override
        public Object right() {
            return right;
        }

        @Override
        public ImmutablePair<Object, Object> immutable() {
            throw new UnsupportedOperationException("not supported on test implementation");
        }
    }

    @Test
    void isEmpty_True() {
        assertTrue(new PairTestImpl(null, null).isEmpty());
    }

    @Test
    void isEmpty_False_Left() {
        assertFalse(new PairTestImpl(new Object(), null).isEmpty());
    }

    @Test
    void isEmpty_False_Right() {
        assertFalse(new PairTestImpl(null, new Object()).isEmpty());
    }

}