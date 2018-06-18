package osd.database.input;

import osd.database.RecordConversion;
import osd.database.RecordAccession;
import osd.database.input.record.RoomTypeRecord;

/**
 * Represents a type of room. Examples include "classroom", "Windows lab", and
 * "darkroom".
 */
public class RoomType extends SchedulingElement {

    @RecordConversion
    RoomType(final RoomTypeRecord record, final RecordAccession recordAccession) {
        super(record.getId(), record.getName());
    }

}
