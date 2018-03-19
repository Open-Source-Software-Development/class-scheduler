package osd.input.placeholder;

import osd.input.Block;

class BlockPlaceholder extends Placeholder implements Block {

    private String id;
    private boolean isA;

    BlockPlaceholder(final String[] row) {
        super(row);
    }

    private BlockPlaceholder(final String id, final boolean isA) {
        super(new String[] {id, isA ? "A" : "B"});
    }

    @FromCSV(0)
    void setId(final String id) {
        this.id = id;
    }

    @FromCSV(1)
    void setSuffix(final String suffix) {
        this.isA = "A".equals(suffix);
    }

    @Override
    public Block getNext() {
        return null;
    }

    @Override
    public Block getPrevious() {
        return null;
    }

    @Override
    public Block getPairedWith() {
        return new BlockPlaceholder(id, isA);
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
