package osd.schedule;

import osd.considerations.Preference;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * A "composite" of all the preferences for a schedule. This gives us a
 * {@link Comparator} which defined {@link #compare(Hunk, Hunk)} in terms of
 * how hunks score on those preferences.
 * <p>This class is immutable.</p>
 * @see Preference
 */
class Preferences implements Comparator<Hunk> {

    private final Collection<Preference> preferences;

    /**
     * DI constructor.
     * @param preferences the preferences for the schedule
     */
    @Inject
    Preferences(final Collection<Preference> preferences) {
        this.preferences = new ArrayList<>(preferences);
    }

    @Override
    public int compare(final Hunk hunk1, final Hunk hunk2) {
        return score(hunk2) - score(hunk1);
    }

    private int score(final Hunk hunk) {
        return preferences.stream()
                .mapToInt(p -> p.evaluate(hunk))
                .sum();
    }

}
