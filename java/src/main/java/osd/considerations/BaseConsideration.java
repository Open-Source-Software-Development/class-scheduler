package osd.considerations;

/**
 * Superinterface for base constraints and base preferences.
 * @param <T> {@link Constraint} or {@link Preference} as appropriate
 */
interface BaseConsideration<T extends Consideration> {

    T bind(final Lookups lookups);

}
