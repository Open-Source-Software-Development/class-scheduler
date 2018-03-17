package osd.schedule;

import osd.input.Block;
import osd.input.Professor;
import osd.input.Room;

import java.util.*;
import java.util.stream.Stream;

/**
 * Assists an {@link Availability} instance in tracking block availability.
 */
class BlockAvailability {

    private final List<Block> blocks;
    private final Set<Key> unavailable;

    /**
     * DI constructor.
     * @param blocks all blocks legal for the section
     */
    BlockAvailability(final Collection<Block> blocks) {
        this.blocks = new ArrayList<>(blocks);
        this.unavailable = new HashSet<>();
    }

    /**
     * Copy constructor. After construction, the original and copy are
     * completely independent.
     * @param copyOf the instance to copy
     */
    BlockAvailability(final BlockAvailability copyOf) {
        this.blocks = new ArrayList<>(copyOf.blocks);
        this.unavailable = new HashSet<>(copyOf.unavailable);
    }

    /**
     * Finds all the blocks where a professor and room are available.
     * @param professor the professor
     * @param room the room
     * @return a stream of all blocks where both are available
     */
    Stream<Block> getAvailable(final Professor professor, final Room room) {
        return blocks.stream()
                .filter(b -> isAvailable(b, professor))
                .filter(b -> isAvailable(b, room));

    }

    /**
     * Indicates that a room is in use during some block.
     * @param block the block
     * @param room the room
     */
    void setUnavailable(final Block block, final Room room) {
        setUnavailable0(block, room);
    }

    /**
     * Indicates that a professor is unavailable at some block.
     * @param block the block
     * @param professor the professor
     */
    void setUnavailable(final Block block, final Professor professor) {
        setUnavailable0(block, professor);
    }

    private void setUnavailable0(final Block block, final Object o) {
        unavailable.add(new Key(block, o));
    }

    private boolean isAvailable(final Block block, final Object o) {
        return !unavailable.contains(new Key(block, o));
    }

    private final class Key {
        private final Block block;
        private final Object roomOrProfessor;

        Key(final Block block, final Object roomOrProfessor) {
            this.block = block;
            this.roomOrProfessor = roomOrProfessor;
        }

        @Override
        public boolean equals(final Object o) {
            if (getClass() == o.getClass()) {
                final Key other = (Key)o;
                return Objects.equals(block, other.block)
                        && Objects.equals(roomOrProfessor, other.roomOrProfessor);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(block, roomOrProfessor);
        }

    }

}
