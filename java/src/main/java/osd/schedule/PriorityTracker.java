package osd.schedule;

import osd.database.Section;
import osd.util.relation.ManyToOneRelation;
import osd.util.relation.OneToManyRelation;

import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A special {@link OneToManyRelation} for section priorities. It associates
 * priorities (lower numbers are higher priority) to sections of that priority.
 * More importantly, it keeps track of the lowest priority number it knows
 * about, and provides accessors to retrieve sections with low priority
 * numbers. (nb. a lower number indicates a higher priority.)
 */
class PriorityTracker extends OneToManyRelation<Long, Section> {

    private Long lowest;

    /**
     * Initializes a PriorityTracker with no data.
     */
    PriorityTracker() {}

    /**
     * Copy constructor. After construction, the original and copy are
     * completely independent.
     * @param copyOf the instance to copy
     */
    PriorityTracker(final PriorityTracker copyOf) {
        super(copyOf);
        lowest = copyOf.lowest;
    }

    /**
     * Finds a section such that no section has a higher priority number.
     * (nb. lower numbers correspond to higher priorities.) If the tracker has
     * no sections, returns {@code null}.
     * @return a section with highest priority, or null
     */
    Set<Section> getHighPrioritySections() {
        final Set<Section> result = get(lowest);
        assert result == null || !result.isEmpty();
        return result;
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
        assert get(key) == null || !get(key).isEmpty();
        if (lowest != null && key <= lowest) {
            updateLowest();
        }
    }

    @Override
    public void remove(final Long key) {
        super.remove(key);
        assert get(key) == null || !get(key).isEmpty();
        if (lowest != null && key <= lowest) {
            updateLowest();
        }
    }

    @Override
    public ManyToOneRelation<Section, Long> reversed() {
        return new ReversedPriorityTracker();
    }

    void ensurePresent(final Section section) {
        if (reversed().get(section) == null) {
            throw new NoSuchElementException(section + " is not pending");
        }
    }

    private void updateLowest() {
        lowest = forward.keySet().stream()
                .min(Long::compareTo)
                .orElse(null);
        assert get(lowest) == null || !get(lowest).isEmpty();
    }

    private final class ReversedPriorityTracker extends ManyToOneRelation<Section, Long> {

        private ReversedPriorityTracker() {
            super(PriorityTracker.this.reverse, PriorityTracker.this.forward);
        }

        @Override
        public OneToManyRelation<Long, Section> reversed() {
            return PriorityTracker.this;
        }

        @Override
        public void remove(final Section section) {
            final Long priority = forward.get(section);
            final boolean needsUpdate = priority != null && lowest != null && priority <= lowest;
            super.remove(section);
            if (needsUpdate) {
                PriorityTracker.this.updateLowest();
            }
        }

    }

}
