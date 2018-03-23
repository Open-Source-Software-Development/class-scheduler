package osd.database;

/**
 * A specific time block, as specified in the sandbox. A block represents a
 * <em>single</em> contiguous unit of time; if two blocks on different days
 * are assigned the same number, they should be differentiated by an
 * alphabetical suffix (eg. 1A and 1B).
 */
public interface Block extends Named {

    /**
     * Gets the next block within the same day. If this is the last block of
     * the day, returns {@code null} instead.
     * @return the next block of the day, or null
     */
    Block getNext();

    /**
     * Gets the previous block within the same day. If this is the first block
     * of the day, returns {@code null} instead.
     * @return the previous block of the day, or null
     */
    Block getPrevious();

    /**
     * Gets the block this one is paired with. For example, block 1A would
     * return 1B.
     * @return the block this one is paired with
     */
    Block getPairedWith();

}
