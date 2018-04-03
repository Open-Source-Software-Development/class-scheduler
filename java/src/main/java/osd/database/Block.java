package osd.database;

/**
 * A specific time block, as specified in the sandbox. A block represents a
 * <em>single</em> contiguous unit of time; if two blocks on different days
 * are assigned the same number, they should be differentiated by an
 * alphabetical suffix (eg. 1A and 1B).
 */
public class Block extends SchedulingElement {

    private final String day;
    private final int hour;

    private RecordLookup lookup;
    private Block next;
    private Block previous;
    private Block pairedWith;

    Block(final String blockId, final String day, final int hour, final RecordLookup lookup) {
        super(blockId);
        this.day = day;
        this.hour = hour;
        this.lookup = lookup;
    }

    /**
     * Gets the next block within the same day. If this is the last block of
     * the day, returns {@code null} instead.
     * @return the next block of the day, or null
     */
    public Block getNext() {
        init();
        return next;
    }

    /**
     * Gets the previous block within the same day. If this is the first block
     * of the day, returns {@code null} instead.
     * @return the previous block of the day, or null
     */
    public Block getPrevious() {
        init();
        return previous;
    }

    /**
     * Gets the block this one is paired with. For example, block 1A would
     * return 1B.
     * @return the block this one is paired with
     */
    public Block getPairedWith() {
        init();
        return pairedWith;
    }

    private String getPairBlockId() {
        final String blockId = getName();
        final int len = blockId.length();
        final String prefix = blockId.substring(0, len - 2);
        final char suffix = blockId.charAt(len - 1);
        final String otherSuffix = (suffix == 'A' ? "B" : "A");
        return prefix + otherSuffix;
    }

    private void init() {
        if (lookup == null) {
            return;
        }
        next = lookup.getAll(Block.class)
                .filter(block -> day.equals(block.day))
                .filter(block -> block.hour == hour + 1)
                .findFirst().orElse(null);
        previous = lookup.getAll(Block.class)
                .filter(block -> day.equals(block.day))
                .filter(block -> block.hour == hour - 1)
                .findFirst().orElse(null);
        pairedWith = lookup.getAll(Block.class)
                .filter(block -> block.getName().equals(getPairBlockId()))
                .findFirst().orElse(null);
        lookup = null;
    }

}
