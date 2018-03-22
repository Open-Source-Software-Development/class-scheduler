package osd.considerations;

import osd.input.*;
import osd.output.Hunk;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Inspects hunks to determine whether they "contain" certain values. The
 * precise meaning of "contain" varies depending on what type of value we're
 * dealing with:
 * <ul>
 *     <li>{@link Section}: Is the hunk for the indicated section?</li>
 *     <li>{@link Professor}: Is the indicated professor teaching the hunk?</li>
 *     <li>{@link Room}: Is the hunk at the indicated room?</li>
 *     <li>{@link Block}: Is the hunk taught during the indicated block?</li>
 *     <li>{@link Course}: Is the hunk's section a section of the course?</li>
 *     <li>{@link RoomType}: Is the hunk's room of the indicated type?</li>
 * </ul>
 * Since hunks may be "incomplete", we must gracefully handle the possibility
 * of {@code null} values. Therefore, if an extraction returns {@code null},
 * we return a special value to indicate that. Otherwise, we can simply check if
 * the extracted value is (or contains) the requested value.
 * @param <T> internal use
 * @see #of(Object) API entry point
 */
class HunkExtractor<T> {

    private static Map<Class<?>, HunkExtractor<?>> INSTANCES = new HashMap<>();

    private final Function<Hunk, T> extractor;
    private final BiPredicate<T, Object> test;

    private HunkExtractor(final Class<?> clazz, final Function<Hunk, T> extractor, final BiPredicate<T, Object> test) {
        this.extractor = extractor;
        this.test = test;
        INSTANCES.put(clazz, this);
    }

    /**
     * Gets a function to check a hunk for some value. The returned function
     * obeys the following contract:
     * <ul>
     *     <li>If the hunk is incomplete such that a result cannot be
     *     determined, return {@link Extraction#INCONCLUSIVE}.</li>
     *     <li>If the hunk contains the requested value, return {@link Extraction#MATCH}.</li>
     *     <li>Otherwise, return {@link Extraction#NO_MATCH}.</li>
     * </ul>
     * <p>An example of an inconclusive match is when the object to look for is
     * a professor, and the hunk's {@link Hunk#getProfessor()} method returns
     * {@code null}.</p>
     * @param testFor the object to test for
     * @return a function testing hunks for that object
     */
    static Function<Hunk, Extraction> of(final Object testFor) {
        final HunkExtractor<?> extractor = get(testFor);
        return h -> extractor.test(h, testFor);
    }

    /**
     * Extraction results.
     * @see Extraction#MATCH
     * @see Extraction#NO_MATCH
     * @see Extraction#INCONCLUSIVE
     */
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
                // In theory, hunks can't have null sections. In practice,
                // it comes up with mocking so often that it's easier to
                // just do a null check anyway.
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

}
