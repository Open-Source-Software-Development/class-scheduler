package osd.database.input;

import osd.database.input.record.RoomTypeRecord;

/**
 * Represents a type of room. Examples include "classroom", "Windows lab", and
 * "darkroom".
 */
public class RoomType extends SchedulingElement {

    @RecordConversion
    RoomType(final RoomTypeRecord record, final RecordConverter recordConverter) {
        super(record.getId(), record.getName());
    }

}
