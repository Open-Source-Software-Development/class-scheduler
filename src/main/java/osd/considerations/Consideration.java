package osd.considerations;

import osd.output.Hunk;

/**
 * Superinterface for user preferences and user constraints.
 */
public interface Consideration<T> extends Function<Hunk, T> {

	T evaluate(final Hunk hunk);

	default T apply(final Hunk hunk) {
		return evaulate(hunk);
	}
}
