package osd.input;

/**
 * Represents a specific room.
 */
public interface Room extends Named {

    /**
     * Determines what type of room this is.
     * @return what type of room this is
     */
	RoomType getRoomType();

}
