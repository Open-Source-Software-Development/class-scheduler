package osd.considerations.base;

import osd.considerations.BasePreference;
import osd.considerations.Lookups;
import osd.input.Block;
import osd.input.Professor;
import osd.output.Hunk;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A base preference that discourages back-to-back-to-back courses.
 */
class AvoidThreeConsecutiveBlocks implements BasePreference {

    @Override
    public int worth() {
        // Feel free to tinker with this value, but for
        // the base preference to have desired behavior,
        // the worth needs to be negative... otherwise
        // we'd *encourage* back-to-back-to-back classes.
        return -5;
    }

    @Override
    public Predicate<Hunk> bindPredicate(final Lookups lookups) {
        return h -> {
            final Professor professor = h.getProfessor();
            final Block block = h.getBlock();
            // Get the set of all blocks this professor is teaching,
            // including the new one (remember that this is called
            // *before* the new hunk gets written into the schedule).
            // Why Stream.concat() instead of just making a set and
            // adding to it? Because Collectors.toSet() doesn't guarantee
            // mutation operations will be supported!
            final Set<Block> blocks = Stream.concat(
                        lookups.lookup(professor).map(Hunk::getBlock),
                        Stream.of(block))
                    .collect(Collectors.toSet());
            // Look at the new block and both adjacent blocks. For any
            // of them, does our set contain all three? If so, we meet
            // the predicate (and *deduct* points from the score).
            return streamAdjacentBlocks(h.getBlock())
                    .filter(Objects::nonNull)
                    .anyMatch(b -> containsBothAdjacentBlocks(blocks, b));
        };
    }

    private static Stream<Block> streamAdjacentBlocks(final Block block) {
        return Stream.of(block, block.getNext(), block.getPrevious());
    }

    private static boolean containsBothAdjacentBlocks(final Set<Block> blocks, final Block block) {
        return blocks.contains(block.getNext())
                && blocks.contains(block.getPrevious());
    }

}
