package osd.database.placeholder;

import osd.database.Block;

import java.util.HashMap;
import java.util.Map;

class BlockPlaceholder extends Placeholder implements Block {

    private int id;
    private boolean isA;

    private static final Map<String, Map<Integer, Block>> CALENDAR = new HashMap<>();
    private String dayOfWeek;
    private int hour;

    BlockPlaceholder(final String[] row) {
        this(row, true);
    }

    private BlockPlaceholder(final String[] row, final boolean write) {
        super(row);
        if (write) {
            CALENDAR.computeIfAbsent(dayOfWeek, unused -> new HashMap<>()).put(this.hour, this);
        }
    }

    @FromCSV(0)
    void setId(final String id) {
        this.id = Integer.valueOf(id);
    }

    @FromCSV(1)
    void setSuffix(final String suffix) {
        this.isA = "A".equals(suffix);
    }

    @FromCSV(2)
    void setDayOfWeek(final String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @FromCSV(3)
    void setHour(final String hour) {
        this.hour = Integer.valueOf(hour);
    }

    @Override
    public Block getNext() {
        return CALENDAR.get(dayOfWeek).get(hour + 1);
    }

    @Override
    public Block getPrevious() {
        return CALENDAR.get(dayOfWeek).get(hour + 1);
    }

    @Override
    public Block getPairedWith() {
        return new BlockPlaceholder(new String[] {id + "", isA ? "B" : "A", dayOfWeek, hour + ""}, false);
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
