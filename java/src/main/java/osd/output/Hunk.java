package osd.output;

import osd.input.*;

import java.util.Objects;

public class Hunk {

    private final Section section;
    private final Professor professor;
    private final Room room;
    private final Block block;

    Hunk(final Section section, final Professor professor, final Room room, final Block block) {
        Objects.requireNonNull(section);
        Objects.requireNonNull(professor);
        Objects.requireNonNull(room);
        Objects.requireNonNull(block);
        this.section = section;
        this.professor = professor;
        this.room = room;
        this.block = block;
    }

    public Section getSection() {
        return section;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Room getRoom() {
        return room;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public String toString() {
        return "Hunk(" + section + ", " + professor + ", " + room + ", " + block + ")";
    }

}
