package osd.considerations;

import osd.output.Hunk;

import java.util.function.Function;

/**
 * Superinterface for user preferences and user constraints.
 */
public interface Consideration<T> extends Function<Hunk, T> {

	T evaluate(final Hunk hunk);

	default T apply(final Hunk hunk) {
		return evaluate(hunk);
	}

}
