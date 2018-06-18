package osd.considerations;

import osd.schedule.Lookups;

/**
 * Superinterface for rules constraints and rules preferences. Unlike user
 * considerations, rules considerations are not referentially transparent.
 * For example, determining whether or not a professor is teaching too many
 * courses is a function of the entire schedule, not just the hunk being
 * tested. Base considerations, therefore, are implemented as a "wrapper"
 * around the normal consideration types, and produce them when given
 * {@linkplain Lookups the full scheduling data}.
 * <p>Note that these considerations are bound and evaluated <em>before</em>
 * the hunk is added to the schedule. In practice, this means that the lookups
 * will <em>not</em> find the hunk that's about to be added. Be careful!!</p>
 * @param <T> {@link Constraint} or {@link Preference} as appropriate
 */
interface BaseConsideration<T> {

    /**
     * Binds this rules consideration to the current schedule data.
     * <p>Example implementation:
     * {@code return h -> {
     *     final Professor professor = h.getProfessor();
     *     final long count = lookups.lookup(professor).count();
     *     return count < profess.getCourseCapacity();
     * }}</p>
     * @param lookups {@link Lookups} representing the current schedule
     * @return a predicate
     */
    T bind(final Lookups lookups);

}
