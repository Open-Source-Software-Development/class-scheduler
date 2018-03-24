package osd.schedule;

import osd.database.Block;
import osd.database.Professor;
import osd.database.Room;
import osd.output.Hunk;
import osd.util.ImmutablePair;
import osd.util.Pair;

import java.util.*;
import java.util.stream.Stream;

/**
 * Assists an {@link CandidateHunkSource} instance in tracking block availability.
 */
class BlockAvailability {

    private final List<Block> blocks;
    private final Set<Pair<Block, Object>> unavailable;

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

    void setUnavailable(final Hunk hunk) {
        hunk.getBlocks().forEach(block -> {
            unavailable.add(ImmutablePair.of(block, hunk.getProfessor()));
            unavailable.add(ImmutablePair.of(block, hunk.getRoom()));
        });
    }

    private boolean isAvailable(final Block block, final Object o) {
        return !unavailable.contains(ImmutablePair.of(block, o));
    }

}
