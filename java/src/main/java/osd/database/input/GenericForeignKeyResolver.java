package osd.database.input;

import javax.inject.Inject;
import java.util.NoSuchElementException;

class GenericForeignKeyResolver {

    @Inject
    GenericForeignKeyResolver() {}

    Class<? extends SchedulingElement> resolve(final int typeId) {
        switch(typeId) {
            case 7: return Block.class;
            case 8: return Course.class;
            case 12: return Professor.class;
            case 14: return Room.class;
            case 15: return RoomType.class;
            default: throw new NoSuchElementException("Unknown type ID " + typeId);
        }
    }

}
