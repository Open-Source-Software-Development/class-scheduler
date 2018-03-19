package osd.schedule;

import osd.input.Professor;
import osd.input.Room;
import osd.input.Section;
import osd.input.Sources;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Priority extends Availability {

    private final PriorityTracker data;

    @Inject
    Priority(final Sources sources, final Constraints constraints) {
        super(sources, constraints);
        this.data = new PriorityTracker();
        sources.getSections().forEach(this::initSection);
    }

    private Priority(final Priority rebind, final Results bindTo) {
        super(rebind, bindTo);
        this.data = new PriorityTracker(rebind.data);
    }

    @Override
    void onHunkAdded(final Hunk hunk) {
        final Section section = hunk.getSection();
        data.ensurePresent(section);
        // Find all the hunks whose priority we'll need to recompute.
        final Collection<Section> impact = getImpacted(hunk);
        super.onHunkAdded(hunk);
        // We no longer need to track this section.
        data.reversed().remove(section);
        // Update all the sections we found before.
        impact.forEach(this::updateSection);
    }

    /**
     * Computes a "high priority" section. A section's priority is defined
     * according to the following algorithm:
     * <ol>
     * <li>Find all rooms that this section can be scheduled at.</li>
     * <li>Find all professors that can teach this section.</li>
     * <li>For each professor, room pair, count all the blocks that pair is
     * available at.</li>
     * <li>Sum those counts.</li>
     * </ol>
     * Lower numbers are higher priority. Therefore, this method shall return
     * a section such that no section yields a lower number. If all sections
     * have been scheduled, this returns {@code null}.
     * @return a "high priority" section
     */
    Section getHighPrioritySection() {
        // If our data is empty, we're done!
        if (data.isEmpty()) {
            return null;
        }
        assert data.getHighPrioritySections() != null;
        assert !data.getHighPrioritySections().isEmpty();
        // Find some arbitrary member of the high-priority set.
        return data.getHighPrioritySections().iterator().next();
    }

    Priority rebind(final Results results) {
        return new Priority(this, results);
    }

    @Override
    public String toString() {
        return data.reversed().toString();
    }

    // Finds all the sections whose priority would be impacted by adding
    // this one. The hunk's section itself is not included.
    private Collection<Section> getImpacted(final Hunk hunk) {
        final Section section = hunk.getSection();
        final Professor professor = hunk.getProfessor();
        final Room room = hunk.getRoom();
        return Stream.concat(
                professors.reversed().getOrDefault(professor, Collections::emptySet).stream(),
                rooms.reversed().getOrDefault(room, Collections::emptySet).stream()
        ).filter(s -> !section.equals(s))
                .unordered()
                .distinct()
                .collect(Collectors.toList());
    }

    private void initSection(final Section section) {
        update0(section);
    }

    private void updateSection(final Section section) {
        data.ensurePresent(section);
        update0(section);
    }

    private void update0(final Section section) {
        long count = getCandidateHunks(section).count();
        data.add(count, section);
    }

}
