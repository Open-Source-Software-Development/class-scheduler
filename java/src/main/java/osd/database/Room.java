package osd.database;

/**
 * Represents a specific room.
 */
public class Room extends SchedulingElement {

    private final RoomType roomType;

    Room(final String name, final RoomType roomType) {
        super(name);
        this.roomType = roomType;
    }

    /**
     * Determines what type of room this is.
     * @return what type of room this is
     */
	public RoomType getRoomType() {
	    return roomType;
    }

}
