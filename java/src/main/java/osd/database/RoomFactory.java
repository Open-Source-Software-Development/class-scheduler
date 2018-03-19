package osd.database;

import osd.input.Room;

import javax.inject.Inject;

public class RoomFactory implements DatabaseFactory<RoomRecord, Room> {

	@Inject
    RoomFactory(/* Add arguments here, if needed.*/) {
		// Do any setup you need here.
	}

	@Override
	public Room create(final RoomRecord record) {
		// TODO: implement this!
		throw new UnsupportedOperationException("not yet implemented");
	}

}
