package osd.considerations;

public interface BasePreference extends BaseConsideration<Preference> {

    /**
     * Determines the "worth" of this rules preference.
     * @return the "worth" of this rules preference
     * @see Preference#worth()
     */
    int worth();

}
