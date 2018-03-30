package osd.considerations;

import osd.database.*;
import osd.schedule.Hunk;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Represents fields of hunks. The basic fields are given by the getter methods
 * of {@link Hunk}. Fields corresponding to courses and room types are also
 * defined.
 * <p>A secondary function of hunk fields is to encapsulate access of their
 * values. In particular, it is often desirable to check whether a hunk
 * "contains" a value. Since the precise meaning of "contain" varies depending
 * on what type of value we're dealing with, this is actually not a trivial
 * abstraction:
 * <ul>
 *     <li>{@link Section}: Is the hunk for the indicated section?</li>
 *     <li>{@link Professor}: Is the indicated professor teaching the hunk?</li>
 *     <li>{@link Room}: Is the hunk at the indicated room?</li>
 *     <li>{@link Block}: Is the hunk taught during the indicated block?</li>
 *     <li>{@link Course}: Is the hunk's section a section of the course?</li>
 *     <li>{@link RoomType}: Is the hunk's room of the indicated type?</li>
 * </ul></p>
 * <p>Since hunks may be "incomplete", we must gracefully handle the possibility
 * of {@code null} values. Therefore, if an extraction returns {@code null},
 * we return a special value to indicate that. Otherwise, we can simply check if
 * the extracted value is (or contains) the requested value.</p>
 * @param <T> internal use
 * @see #get(Object) API entry point
 * @see #getExtractor(Object) check if fields contain values
 */
class HunkField<T> {

    private static Map<Class<?>, HunkField<?>> INSTANCES = new HashMap<>();

    private final Function<Hunk, T> extractor;
    private final BiPredicate<T, Object> test;

    private HunkField(final Class<?> clazz, final Function<Hunk, T> extractor, final BiPredicate<T, Object> test) {
        this.extractor = extractor;
        this.test = test;
        INSTANCES.put(clazz, this);
    }

    /**
     * Finds a hunk field that could contain an object of some type.
     * @param object an object of the desired type
     * @return a hunk field for that type
     * @throws IllegalArgumentException if there is no field for that type
     */
    static HunkField<?> get(final Object object) {
        for (final Map.Entry<Class<?>, HunkField<?>> entry: INSTANCES.entrySet()) {
            final Class<?> clazz = entry.getKey();
            if (clazz.isInstance(object)) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException("couldn't find a hunk field corresponding to " + object);
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
    Function<Hunk, Extraction> getExtractor(final Object testFor) {
        final HunkField<?> extractor = get(testFor);
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
    private static final HunkField<Section> SECTION =
            new HunkField<>(Section.class, Hunk::getSection, Object::equals);

    @SuppressWarnings("unused")
    private static final HunkField<Professor> PROFESSOR =
            new HunkField<>(Professor.class, Hunk::getProfessor, Object::equals);

    @SuppressWarnings("unused")
    private static final HunkField<Room> ROOM =
            new HunkField<>(Room.class, Hunk::getRoom, Object::equals);

    @SuppressWarnings("unused")
    private static final HunkField<Collection<Block>> BLOCK =
            new HunkField<>(Block.class, Hunk::getBlocks, Collection::contains);

    @SuppressWarnings("unused")
    private static final HunkField<RoomType> ROOM_TYPE =
            new HunkField<>(RoomType.class, hunk -> {
                final Room room = hunk.getRoom();
                return room == null ? null : room.getRoomType();
            }, Object::equals);

    @SuppressWarnings("unused")
    private static final HunkField<Course> COURSE =
            new HunkField<>(Course.class, hunk -> {
                final Section section = hunk.getSection();
                // In theory, hunks can't have null sections. In practice,
                // it comes up with mocking so often that it's easier to
                // just do a null check anyway.
                return section == null ? null : section.getCourse();
            }, Object::equals);

    private Extraction test(final Hunk hunk, final Object testFor) {
        final T extracted = extractor.apply(hunk);
        if (extracted == null) {
            return Extraction.INCONCLUSIVE;
        }
        boolean match = test.test(extracted, testFor);
        return match ? Extraction.MATCH : Extraction.NO_MATCH;
    }

}
