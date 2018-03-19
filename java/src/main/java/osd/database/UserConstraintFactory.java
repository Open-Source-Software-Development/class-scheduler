package osd.database;

import osd.considerations.UserConstraint;

import javax.inject.Inject;

public class UserConstraintFactory implements DatabaseFactory<UserConstraintRecord, UserConstraint> {

	@Inject
    UserConstraintFactory(/* Add arguments here, if needed.*/) {
		// Do any setup you need here.
	}

	@Override
	public UserConstraint create(final UserConstraintRecord record) {
		// TODO: implement this!
		throw new UnsupportedOperationException("not yet implemented");
	}

}
