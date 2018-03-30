package osd.schedule;

import osd.database.Block;
import osd.database.Professor;
import osd.database.Room;
import osd.database.Section;

import java.util.*;

/**
 * Represents a single scheduler schedule. A hunk is defined by a
 * {@code (Section, Professor, Room, Block}) 4-tuple. This is sufficient to
 * provide all scheduling data needed for the specified section.
 */
public class Hunk {

    private final Section section;
    private final Professor professor;
    private final Room room;
    private final Set<Block> blocks;

    public Hunk(final Section section, final Professor professor, final Room room, final Collection<Block> blocks) {
        Objects.requireNonNull(section);
        this.section = section;
        this.professor = professor;
        this.room = room;
        this.blocks = blocks == null ? null : Collections.unmodifiableSet(new HashSet<>(blocks));
        assert blocks == null || this.blocks.size() == blocks.size() : "hunk created with duplicate block";
    }

    /**
     * Gets the course section this hunk provides scheduling data for.
     * @return the course section this hunk provides scheduling data for
     */
    public Section getSection() {
        return section;
    }

    /**
     * Gets the professor slated to teach the {@linkplain #getSection() section}.
     * @return the professor slated to teach the section
     */
    public Professor getProfessor() {
        return professor;
    }


    /**
     * Gets the room the {@linkplain #getSection() section} is scheduled at.
     * @return the room the section is scheduled at
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Gets the time block the {@linkplain #getSection() section} is scheduled at.
     * @return the time block the section is scheduled at
     */
    public Set<Block> getBlocks() {
        return blocks;
    }

    boolean validateBlocks() {
        return blocks == null || blocks.stream().allMatch(Objects::nonNull);
    }

    @Override
    public String toString() {
        return "Hunk(" + section + ", " + professor + ", " + room + ", " + blocks + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hunk hunk = (Hunk) o;
        return Objects.equals(section, hunk.section) &&
                Objects.equals(professor, hunk.professor) &&
                Objects.equals(room, hunk.room) &&
                Objects.equals(blocks, hunk.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section, professor, room, blocks);
    }
}
