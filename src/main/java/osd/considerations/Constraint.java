package osd.considerations;

import osd.output.hunk;

public interface Constraint extends Consideration<Boolean>, Predicate<Hunk> {

	static enum Type {
		WHITELIST, BLACKLIST;
	}

	Type getType();

	static Constraint of(final Predicate<Boolean> predicate, final Type type) {
		switch (type) {
		case WHITELIST:
			return (Whitelist)predicate::test;
		case BLACKLIST:
			return (Blacklist)predicate::test;
		default:
			throw new AssertionError();
		}
	};

	interface Blacklist extends Constraint {

		@Override
		default Type getType() {
			return Type.BLACKLIST;
		}

	}

	interface Whitelist extends Constraint {

		@Override
		default Type getType() {
			return Type.WHITELIST;
		}

	}

	default boolean test(final Hunk hunk) {
		return evaluate(hunk);
	}

}
