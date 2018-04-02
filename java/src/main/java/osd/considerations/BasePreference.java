package osd.considerations;

public interface BasePreference extends BaseConsideration<Preference> {

    /**
     * Determines the "worth" of this base preference.
     * @return the "worth" of this base preference
     * @see Preference#worth()
     */
    int worth();

}
