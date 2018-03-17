package osd.input;

import java.util.stream.Stream;

public interface Sources {

    Stream<Section> getSections();

    Stream<Professor> getProfessors();

    Stream<Block> getBlocks();

    Stream<Room> getRooms();

}
