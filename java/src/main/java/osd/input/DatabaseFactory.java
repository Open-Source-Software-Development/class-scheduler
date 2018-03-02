package osd.input;

import java.util.function.Function;

/**
 * Converts some kind of database record to its internal representation. For
 * example, a {@code DatabaseFactory<ProfessorRecord, Professor>} would be
 * responsible for converting professor records.
 * @param <R> the record type to convert
 * @param <O> the type that internally represents that record type
 */
@FunctionalInterface
interface DatabaseFactory<R, O> extends Function<R, O> {

    /**
     * Converts some record.
     * @param record the record to convert
     * @return the converted record
     */
	O create(final R record);

	default O apply(final R record) {
		return create(record);
	}

}
