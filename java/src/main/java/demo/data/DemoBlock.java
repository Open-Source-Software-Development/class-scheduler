package demo.data;

import osd.input.Block;

public enum DemoBlock implements Block, DemoNamed {

    BLOCK_1A,
    BLOCK_1B,

    BLOCK_2A,
    BLOCK_2B,

    BLOCK_3A,
    BLOCK_3B,

    BLOCK_4A,
    BLOCK_4B,

    BLOCK_5A,
    BLOCK_5B,

    BLOCK_6A,
    BLOCK_6B,
    ;

    @Override
    public Block getNext() {
        try {
            return values()[ordinal() + 2];
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public Block getPrevious() {
        try {
            return values()[ordinal() - 2];
        } catch (final ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public Block getPairedWith() {
        final int offset = ((ordinal() % 2) == 0) ? 1 : -1;
        return values()[ordinal() + offset];
    }

}
