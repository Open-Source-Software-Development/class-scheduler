package osd.considerations;

import osd.schedule.Hunk;
import osd.schedule.Lookups;

import java.util.function.Predicate;

/**
 * Superinterface for base constraints and base preferences. Unlike user
 * considerations, base considerations are not referentially transparent.
 * For example, determining whether or not a professor is teaching too many
 * courses is a function of the entire schedule, not just the hunk being
 * tested. Base considerations, therefore, are implemented as a "wrapper"
 * around the normal consideration types, and produce them when given
 * {@linkplain Lookups the full scheduling data}.
 * <p>Note that these considerations are bound and evaluated <em>before</em>
 * the hunk is added to the schedule. In practice, this means that the lookups
 * will not find it.</p>
 * @param <T> {@link Constraint} or {@link Preference} as appropriate
 */
interface BaseConsideration<T extends Consideration> {

    /**
     * Binds this base consideration to the current schedule data.
     * <p>Example implementation:
     * {@code return h -> {
     *     final Professor professor = h.getProfessor();
     *     final long count = lookups.lookup(professor).count();
     *     return count < profess.getCourseCapacity();
     * }}</p>
     * @param lookups {@link Lookups} representing the current schedule
     * @return a predicate
     */
    Predicate<Hunk> bindPredicate(final Lookups lookups);

    /**
     * Converts the {@link #bindPredicate(Lookups)} to an actual consideration.
     * This is mostly a convenience to ensure implementations of subinterfaces
     * are eligible for lambda syntax when implementing that method.
     * @param lookups {@link Lookups} representing the current schedule
     * @return an appropriate consideration
     */
    T bind(final Lookups lookups);

}
