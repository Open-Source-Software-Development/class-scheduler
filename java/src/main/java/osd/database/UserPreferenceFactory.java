package osd.database;

import osd.considerations.UserPreference;

import javax.inject.Inject;

public class UserPreferenceFactory implements DatabaseFactory<UserPreferenceRecord, UserPreference> {

	@Inject
    UserPreferenceFactory(/* Add arguments here, if needed.*/) {
		// Do any setup you need here.
	}

	@Override
	public UserPreference create(final UserPreferenceRecord record) {
		// TODO: implement this!
		throw new UnsupportedOperationException("not yet implemented");
	}

}
