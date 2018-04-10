package osd.database.input;

import osd.database.RecordAccession;
import osd.database.RecordConversion;
import osd.database.input.record.RoomRecord;

/**
 * Represents a specific room.
 */
public class Room extends SchedulingElement {

    private final RoomType roomType;

    @RecordConversion
    Room(final RoomRecord record, final RecordAccession recordAccession) {
        super(record.getId(), record.getBuilding() + "-" + record.getRoomNumber());
        this.roomType = recordAccession.get(RoomType.class, record.getRoomTypeId());
    }

    /**
     * Determines what type of room this is.
     * @return what type of room this is
     */
	public RoomType getRoomType() {
	    return roomType;
    }

}
