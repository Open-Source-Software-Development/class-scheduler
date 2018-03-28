package osd.considerations;

import osd.schedule.Hunk;
import osd.schedule.Lookups;

import java.util.function.Predicate;

public interface BasePreference extends BaseConsideration<Preference> {

    /**
     * Determines the "worth" of this base preference.
     * @return the "worth" of this base preference
     * @see Preference#worth()
     */
    int worth();

    @Override
    default Preference bind(final Lookups lookups) {
        final Predicate<Hunk> predicate = bindPredicate(lookups);
        return new Preference() {
            @Override
            public int worth() {
                return BasePreference.this.worth();
            }

            @Override
            public boolean test(final Hunk hunk) {
                return predicate.test(hunk);
            }
        };
    }

}
