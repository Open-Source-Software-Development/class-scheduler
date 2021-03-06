package osd.considerations.rules;

import osd.considerations.BasePreference;
import osd.considerations.Preference;
import osd.schedule.Lookups;
import osd.database.input.Block;
import osd.database.input.Professor;
import osd.schedule.Hunk;

import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * A rules preference that discourages back-to-back-to-back courses.
 */
class AvoidThreeConsecutiveBlocks implements BasePreference {

    @Override
    public int worth() {
        // Feel free to tinker with this value, but for
        // the rules preference to have desired behavior,
        // the worth needs to be negative... otherwise
        // we'd *encourage* back-to-back-to-back classes.
        return -5;
    }

    @Override
    public Preference bind(final Lookups lookups) {
        return Preference.of(h -> test(h, lookups), worth());
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
