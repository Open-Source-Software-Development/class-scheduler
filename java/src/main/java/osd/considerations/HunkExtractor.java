package osd.considerations;

import osd.input.*;
import osd.output.Hunk;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;

class HunkExtractor<T> {

    private static Map<Class<?>, HunkExtractor<?>> INSTANCES = new HashMap<>();

    private final Function<Hunk, T> extractor;
    private final BiPredicate<T, Object> test;

    private HunkExtractor(final Class<?> clazz, final Function<Hunk, T> extractor, final BiPredicate<T, Object> test) {
        this.extractor = extractor;
        this.test = test;
        INSTANCES.put(clazz, this);
    }

    static Function<Hunk, Extraction> of(final Object testFor) {
        final HunkExtractor<?> extractor = get(testFor);
        return h -> extractor.test(h, testFor);
    }

    @SuppressWarnings("unused")
    private static final HunkExtractor<Section> SECTION =
            new HunkExtractor<>(Section.class, Hunk::getSection, Object::equals);

    @SuppressWarnings("unused")
    private static final HunkExtractor<Professor> PROFESSOR =
            new HunkExtractor<>(Professor.class, Hunk::getProfessor, Object::equals);

    @SuppressWarnings("unused")
    private static final HunkExtractor<Room> ROOM =
            new HunkExtractor<>(Room.class, Hunk::getRoom, Object::equals);

    @SuppressWarnings("unused")
    private static final HunkExtractor<Collection<Block>> BLOCK =
            new HunkExtractor<>(Block.class, Hunk::getBlocks, Collection::contains);

    @SuppressWarnings("unused")
    private static final HunkExtractor<RoomType> ROOM_TYPE =
            new HunkExtractor<>(RoomType.class, hunk -> {
                final Room room = hunk.getRoom();
                return room == null ? null : room.getRoomType();
            }, Object::equals);

    @SuppressWarnings("unused")
    private static final HunkExtractor<Course> COURSE =
            new HunkExtractor<>(Course.class, hunk -> {
                final Section section = hunk.getSection();
                return section == null ? null : section.getCourse();
            }, Object::equals);

    private static HunkExtractor<?> get(final Object testFor) {
        for (final Map.Entry<Class<?>, HunkExtractor<?>> entry: INSTANCES.entrySet()) {
            final Class<?> clazz = entry.getKey();
            if (clazz.isInstance(testFor)) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException("couldn't find a hunk extractor corresponding to " + testFor);
    }

    private Extraction test(final Hunk hunk, final Object testFor) {
        final T extracted = extractor.apply(hunk);
        if (extracted == null) {
            return Extraction.INCONCLUSIVE;
        }
        boolean match = test.test(extracted, testFor);
        return match ? Extraction.MATCH : Extraction.NO_MATCH;
    }

    enum Extraction {
        /**
         * The hunk contained the object we're looking for.
         */
        MATCH,

        /**
         * The hunk did not contain the object we're looking for.
         */
        NO_MATCH,

        /**
         * We found {@code null} when looking for the object (incomplete hunk).
         */
        INCONCLUSIVE,
    }

}
