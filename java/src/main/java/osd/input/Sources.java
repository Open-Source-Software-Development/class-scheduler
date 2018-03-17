package osd.input;

import osd.input.Block;
import osd.input.Professor;
import osd.input.Room;
import osd.input.Section;

import java.util.stream.Stream;

public interface Sources {

    Stream<Section> getSections();

    Stream<Professor> getProfessors();

    Stream<Block> getBlocks();

    Stream<Room> getRooms();

}
