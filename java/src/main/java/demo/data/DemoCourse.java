package demo.data;

import osd.input.Course;
import osd.input.RoomType;
import osd.input.Section;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum DemoCourse implements Course, DemoNamed {

    CSI_101(DemoRoomType.CLASSROOM, 4),
    CSI_102(DemoRoomType.CLASSROOM, 4),
    CSI_201(DemoRoomType.WINDOWS_LAB, 3),
    CSI_202(DemoRoomType.WINDOWS_LAB, 2),
    CSI_310(DemoRoomType.AUDITORIUM, 1),
    CSI_404(DemoRoomType.CLASSROOM, 2),
    CSI_666(DemoRoomType.MAC_LAB, 1), // Macs are hell

    ;

    private final RoomType roomType;
    private final int sectionCount;

    DemoCourse(final RoomType roomType, final int sectionCount) {
        this.roomType = roomType;
        this.sectionCount = sectionCount;
    }

    @Override
    public Iterable<Section> getSections() {
        return IntStream.rangeClosed(1, sectionCount)
                .mapToObj(i -> new DemoSection(this, i))
                .collect(Collectors.toList());
    }

}
