package osd.input;

interface DatabaseFactory<R, O> extends Function<R, O> {

	O create(final R record);

	default O apply(final R record) {
		return create(reford);
	}

}
