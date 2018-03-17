package osd.schedule;

import osd.input.Section;
import osd.input.Sources;
import osd.output.Hunk;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

class Scheduler {

    private final PriorityTracker sections;

    private final Availability availability;
    private final Constraints constraints;
    private final Preferences preferences;

    /**
     * DI constructor
     * @param sources everything that exists in this schedule
     * @param constraints the constraints for this schedule
     * @param preferences the preferences for this schedule
     * @param availability an {@link Availability instance}
     */
    @Inject
    Scheduler(final Sources sources, final Constraints constraints, final Preferences preferences,
              final Availability availability) {
        sections = new PriorityTracker();
        this.availability = availability;
        this.constraints = constraints;
        this.preferences = preferences;
        sources.getSections().forEach(this::update);
    }

    private Scheduler(final Scheduler copyOf) {
        this.sections = new PriorityTracker(copyOf.sections);
        this.availability = new Availability(copyOf.availability);
        this.constraints = copyOf.constraints;
        this.preferences = copyOf.preferences;
    }

    Scheduler copy() {
        return new Scheduler(this);
    }

    /**
     * Attempts to add a hunk to this scheduler. First, the scheduler checks
     * if it has a hunk for that section already. If it does, the addition
     * fails with an {@link IllegalArgumentException}. Otherwise, the
     * scheduler checks if the hunk violates any constraints. If it does,
     * {@code false} is returned and nothing happens. Otherwise, the section
     * is added and {@code true} is returned.
     * <p>As the algorithm is refined, this definition may be amended to define
     * circumstances in which constraint-violating hunks may be added anyway.
     * If so, the return value shall be {@code true} in those cases.</p>
     * @param hunk the hunk to add to the scheduler
     * @return whether the hunk was added
     */
    boolean addHunk(final Hunk hunk) {
        final Section section = hunk.getSection();
        if (sections.reversed().get(section) == null) {
            throw new IllegalArgumentException(section + " is not pending");
        }
        if (!constraints.test(hunk)) {
            return false;
        }
        final List<Section> impact = availability.getImpacted(hunk).collect(Collectors.toList());
        availability.onHunkAdded(hunk);
        impact.forEach(this::update);
        sections.reversed().remove(section);
        return true;
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
    Section getNextSection() {
        if (sections.isEmpty()) {
            return null;
        }
        assert sections.getHighPriority() != null;
        return sections.getHighPriority().iterator().next();
    }

    /**
     * Gets all the candidate hunks for some section. These are sorted
     * by preferences; hunks that score higher will come before those that
     * score lower.
     * @param section the section to generate candidates for
     * @return the (sorted) candidates for that section
     */
    Iterable<Hunk> getCandidateHunks(final Section section) {
        return () -> availability.candidates(section)
                .sorted(preferences)
                .iterator();
    }

    private void update(final Section section) {
        long priority = availability.candidates(section).count();
        sections.add(priority, section);
    }

}
