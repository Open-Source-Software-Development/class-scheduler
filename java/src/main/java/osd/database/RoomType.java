package osd.database;

/**
 * Represents a type of room. Examples include "classroom", "Windows lab", and
 * "darkroom".
 */
public class RoomType extends SchedulingElement {

    RoomType(final int id, final String name) {
        super(id, name);
    }

}
