package osd.input.placeholder;

import osd.input.Block;

class BlockPlaceholder extends Placeholder implements Block {

    private int id;
    private boolean isA;

    private static int LOWEST_ID = 1;
    private static int HIGHEST_ID = 18;

    BlockPlaceholder(final String[] row) {
        super(row);
    }

    private BlockPlaceholder(final Object id, final boolean isA) {
        super(new String[] {id.toString(), isA ? "A" : "B"});
    }

    @FromCSV(0)
    void setId(final String id) {
        this.id = Integer.valueOf(id);
    }

    @FromCSV(1)
    void setSuffix(final String suffix) {
        this.isA = "A".equals(suffix);
    }

    @Override
    public Block getNext() {
        if (id < HIGHEST_ID) {
            return new BlockPlaceholder(id + 1, isA);
        }
        return null;
    }

    @Override
    public Block getPrevious() {
        if (id > LOWEST_ID) {
            return new BlockPlaceholder(id - 1, isA);
        }
        return null;
    }

    @Override
    public Block getPairedWith() {
        return new BlockPlaceholder(id, !isA);
    }

    @Override
    public String getName() {
        return id + (isA ? "A" : "B");
    }

    @Override
    public String toString() {
        return getName();
    }

}
