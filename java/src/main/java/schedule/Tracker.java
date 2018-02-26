package schedule;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

class Tracker<O> {

    private final Set<O> options = new HashSet<>();

    /**
     * Marks some option as available for this tracker. If the option is already available, this has no effect.
     * Otherwise, subsequent calls to {@link #getOptions()} will include this option.
     * @param option the option that's now available
     */
    void addOption(final O option) {
        options.add(option);
    }

    /**
     * Marks some option as unavailable for this tracker. Subsequent calls to {@link #getOptions()} won't include this
     * option. If the option wasn't already available, nothing happens.
     * @param option the option that's no longer available
     */
    void consumeOption(final O option) {
        options.remove(option);
    }

    /**
     * Gets the options available to this tracker.
     * @return the set of options available to this tracker
     */
    Stream<O> getOptions() {
        return options.stream();
    }

    /**
     * Counts the options available to this tracker.
     * @return the count of options available to this tracker
     */
    long countOptions() {
        return getOptions().count();
    }

}
