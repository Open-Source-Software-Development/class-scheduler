package osd.input.placeholder;

import osd.input.Room;
import osd.input.RoomType;

import java.util.Objects;

class RoomPlaceholder extends NamedPlaceholder implements Room {

    private RoomType roomType;

    RoomPlaceholder(final String[] row) {
        super(row);
    }

    @FromCSV(1)
    void setRoomType(final String roomTypeName) {
        this.roomType = new RoomTypePlaceholder(roomTypeName);
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    private static final class RoomTypePlaceholder implements RoomType {

        private final String name;

        RoomTypePlaceholder(final String name) {
            this.name = name;
        }

        @Override
        public boolean equals(final Object o) {
            if (o instanceof RoomTypePlaceholder) {
                final RoomTypePlaceholder other = (RoomTypePlaceholder)o;
                return Objects.equals(name, other.name);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
