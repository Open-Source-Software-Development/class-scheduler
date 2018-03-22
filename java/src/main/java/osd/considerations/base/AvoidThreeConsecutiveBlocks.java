package osd.considerations.base;

import osd.considerations.BasePreference;
import osd.considerations.Lookups;
import osd.input.Block;
import osd.input.Professor;
import osd.output.Hunk;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
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
        return h -> test(h, lookups);
    }

    private static boolean test(final Hunk hunk, final Lookups lookups) {
        final Professor professor = hunk.getProfessor();
        final Set<Block> alreadyWorkingAt = lookups.lookup(professor)
                .flatMap(h -> h.getBlocks().stream())
                .collect(Collectors.toSet());
        return hunk.getBlocks().stream()
                .allMatch(newBlock -> test(alreadyWorkingAt, newBlock));
    }

    private static boolean test(final Set<Block> blocks, final Block newBlock) {
        return containsBothAdjacent(blocks, newBlock)
                || containsTwoAfter(blocks, newBlock)
                || containsTwoBefore(blocks, newBlock);
    }

    private static boolean containsBothAdjacent(final Set<Block> blocks, final Block anchor) {
        return blocks.contains(anchor.getPrevious()) && blocks.contains(anchor.getNext());
    }

    private static boolean containsTwoBefore(final Set<Block> blocks, final Block anchor) {
        return containsTwoInDirection(blocks, anchor, Block::getPrevious);
    }

    private static boolean containsTwoAfter(final Set<Block> blocks, final Block anchor) {
        return containsTwoInDirection(blocks, anchor, Block::getNext);
    }

    // Convenience so we don't need to think too hard about blocks
    // with null previous/next blocks.
    private static boolean containsTwoInDirection(final Set<Block> blocks, final Block anchor, final UnaryOperator<Block> operator) {
        final Block offset1 = operator.apply(anchor);
        if (offset1 == null) {
            return false;
        }
        final Block offset2 = operator.apply(offset1);
        return offset2 != null && blocks.contains(offset1) && blocks.contains(offset2);
    }

}
