package osd.considerations;

import osd.output.Hunk;

import java.util.function.Function;

/**
 * Superinterface for user preferences and user constraints.
 * @param <T> what sort of decision this consideration makes
 */
public interface Consideration<T> extends Function<Hunk, T> {

	/**
	 * Determines whether the hunk meets this consideration.
	 * @param hunk the hunk to evaluate
	 * @return an indication of whether the hunk meets this consideration
	 */
	T evaluate(final Hunk hunk);

	default T apply(final Hunk hunk) {
		return evaluate(hunk);
	}

}
