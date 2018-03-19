package osd.considerations;

import osd.input.*;
import osd.output.Hunk;

import java.util.function.Function;
import java.util.function.Predicate;

class UserConsideration implements Consideration {

    private final Predicate<Hunk> predicate;

    <L, R> UserConsideration(final Class<L> leftClass, final L left, final Class<R> rightClass, final R right) {
        final Predicate<Hunk> leftPredicate = getPredicate(leftClass, left);
        final Predicate<Hunk> rightPredicate = getPredicate(rightClass, right);
        predicate = h -> !leftPredicate.test(h) || rightPredicate.test(h);
    }

    @Override
    public boolean test(final Hunk hunk) {
        return predicate.test(hunk);
    }

    private static Predicate<Hunk> getPredicate(final Class<?> clazz, final Object o) {
        final Function<Hunk, ?> extractor = getExtractor(clazz);
        return h -> {
            final Object o2 = extractor.apply(h);
            return o2 == null || o2.equals(o);
        };
    }

    private static Function<Hunk, ?> getExtractor(final Class<?> clazz) {
        if (clazz == Professor.class) {
            return Hunk::getProfessor;
        }
        if (clazz == Room.class) {
            return Hunk::getRoom;
        }
        if (clazz == RoomType.class) {
            return h -> h.getRoom().getRoomType();
        }
        if (clazz == Section.class) {
            return Hunk::getSection;
        }
        if (clazz == Course.class) {
            return h -> h.getSection().getCourse();
        }
        throw new IllegalArgumentException("no extractor defined for " + clazz);
    }

}
