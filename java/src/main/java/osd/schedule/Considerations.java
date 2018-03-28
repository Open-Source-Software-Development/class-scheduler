package osd.schedule;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

class Considerations {

    private final Predicate<Hunk> userConstraints;
    private final BiPredicate<Lookups, Hunk> baseConstraints;
    private final BiFunction<Lookups, Hunk, Integer> preferences;

    @Inject
    Considerations(final Predicate<Hunk> userConstraints, final BiPredicate<Lookups, Hunk> baseConstraints,
                   final BiFunction<Lookups, Hunk, Integer> preferences) {
        this.userConstraints = userConstraints;
        this.baseConstraints = baseConstraints;
        this.preferences = preferences;
    }

    /**
     * Get a comparator representing all the preferences for this schedule.
     * Since base preferences are not referentially transparent, we need to
     * supply {@linkplain Lookups some additional context}. The resulting
     * comparator sorts hunks with a higher preference score before those
     * with a lower preference score.
     * @param lookups a {@link Lookups} instance representing the schedule so far
     * @return a comparator in descending preference order
     */
    Comparator<Hunk> getPreferenceComparator(final Lookups lookups) {
        // This gives us a comparator that sorts in *ascending* preference order,
        // ie. the reverse of what we want.
        final Comparator<Hunk> comparator = Comparator.comparing(hunk -> preferences.apply(lookups, hunk));
        // Fortunately, we can reverse the reverse of what we want.
        return comparator.reversed();
    }

    /**
     * Get a predicate representing the user constraints for this schedule.
     * @return a predicate representing the user constraints for this schedule
     */
    Predicate<Hunk> getUserConstraints() {
        return userConstraints;
    }

    /**
     * Get a predicate representing the base constraints for this schedule.
     * Since base constraints are not referentially transparent, we need to
     * supply {@linkplain Lookups some additional context}.
     * @param lookups a {@link Lookups} instance representing the schedule so far
     * @return a predicate representing the base constraints for this schedule
     */
    Predicate<Hunk> getBaseConstraints(final Lookups lookups) {
        return hunk -> baseConstraints.test(lookups, hunk);
    }

}
