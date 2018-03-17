package osd.schedule;

import osd.input.Section;
import osd.util.relation.ManyToOneRelation;
import osd.util.relation.OneToManyRelation;

import java.util.Set;

class PriorityTracker extends OneToManyRelation<Long, Section> {

    private Long lowest;

    PriorityTracker() {}

    PriorityTracker(final PriorityTracker copyOf) {
        super(copyOf);
        lowest = copyOf.lowest;
    }

    Set<Section> getLowest() {
        return get(lowest);
    }

    @Override
    public void add(final Long key, final Section value) {
        super.add(key, value);
        if (lowest == null || lowest > key) {
            updateLowest();
        }
    }

    @Override
    public void remove(final Long key, final Section value) {
        super.remove(key, value);
        if (lowest != null && key <= lowest) {
            updateLowest();
        }
    }

    @Override
    public void remove(final Long key) {
        super.remove(key);
        if (lowest != null && key <= lowest) {
            updateLowest();
        }
    }

    @Override
    public ManyToOneRelation<Section, Long> reversed() {
        return new ReversedPriorityTracker();
    }

    private void updateLowest() {
        lowest = data.keySet().stream()
                .min(Long::compareTo)
                .orElse(null);
    }

    private final class ReversedPriorityTracker extends ManyToOneRelation<Section, Long> {

        private ReversedPriorityTracker() {
            super(PriorityTracker.this, null);
        }

        @Override
        public OneToManyRelation<Long, Section> reversed() {
            return PriorityTracker.this;
        }

        @Override
        public void remove(final Section section) {
            if (!data.containsKey(section)) {
                return;
            }
            final boolean needsUpdate = data.get(section) <= lowest;
            super.remove(section);
            if (needsUpdate) {
                PriorityTracker.this.updateLowest();
            }
        }

    }

    @Override
    public String toString() {
        return data.toString();
    }

}
