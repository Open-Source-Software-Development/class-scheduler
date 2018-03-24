package osd.schedule;

import osd.considerations.BasePreference;
import osd.considerations.Lookups;
import osd.considerations.Preference;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * A "composite" of all the preferences for a schedule. This gives us a
 * {@link Comparator} which defines {@link Comparator#compare(Object, Object)}
 * in terms of how hunks score on those preferences.
 * <p>This class is immutable.</p>
 * @see Preference
 */
class Preferences {

    private final Collection<Preference> preferences;
    private final Collection<BasePreference> basePreferences;

    /**
     * DI constructor.
     * @param preferences the preferences for the schedule
     */
    @Inject
    Preferences(final Collection<Preference> preferences, final Collection<BasePreference> basePreferences) {
        this.preferences = new ArrayList<>(preferences);
        this.basePreferences = new ArrayList<>(basePreferences);
    }

    Comparator<Hunk> bind(final Lookups lookups) {
        return (h1, h2) -> score(h2, lookups) - score(h1, lookups);
    }

    private int score(final Hunk hunk, final Lookups lookups) {
        final int preferencesScore = preferences.stream()
                .mapToInt(p -> p.evaluate(hunk))
                .sum();
        final int basePreferencesScore = basePreferences.stream()
                .map(b -> b.bind(lookups))
                .mapToInt(p -> p.evaluate(hunk))
                .sum();
        return preferencesScore + basePreferencesScore;
    }

}
