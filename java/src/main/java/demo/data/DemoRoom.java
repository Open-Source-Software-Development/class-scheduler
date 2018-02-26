package demo.data;

import osd.input.Room;
import osd.input.RoomType;

public enum DemoRoom implements Room, DemoNamed {

    JOYCE_201(DemoRoomType.CLASSROOM),
    JOYCE_202(DemoRoomType.CLASSROOM),
    JOYCE_211(DemoRoomType.WINDOWS_LAB),
    MIC_308(DemoRoomType.AUDITORIUM),
    WIC_666(DemoRoomType.MAC_LAB)

    ;

    private final RoomType roomType;

    DemoRoom(final RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }
}
