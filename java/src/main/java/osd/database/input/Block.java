package osd.database.input;

import osd.database.RecordConversion;
import osd.database.RecordConverter;
import osd.database.input.record.BlockRecord;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A specific time block, as specified in the sandbox. A block represents a
 * <em>single</em> contiguous unit of time; if two blocks on different days
 * are assigned the same number, they should be differentiated by an
 * alphabetical suffix (eg. 1A and 1B).
 */
public class Block extends SchedulingElement {

    private final String day;
    private final int hour;

    private Supplier<Stream<Block>> allBlocks;
    private Block next;
    private Block previous;
    private Block pairedWith;

    @RecordConversion
    Block(final BlockRecord record, final RecordConverter recordConverter) {
        super(record.getId(), record.getBlockId());
        this.day = record.getDay();
        this.hour = Integer.valueOf(record.getStartTime().split(":")[0]);
        this.allBlocks = () -> recordConverter.getAll(Block.class);
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
        final String prefix = blockId.substring(0, len - 1);
        final char suffix = blockId.charAt(len - 1);
        final String allSuffix = (suffix == 'A' ? "B" : "A");
        return prefix + allSuffix;
    }

    private void init() {
        if (allBlocks == null) {
            return;
        }
        next = allBlocks.get()
                .filter(block -> day.equals(block.day))
                .filter(block -> block.hour == hour + 1)
                .findFirst().orElse(null);
        previous = allBlocks.get()
                .filter(block -> day.equals(block.day))
                .filter(block -> block.hour == hour - 1)
                .findFirst().orElse(null);
        pairedWith = allBlocks.get()
                .filter(block -> block.getName().equals(getPairBlockId()))
                .findFirst().orElse(null);
        allBlocks = null;
    }

}
