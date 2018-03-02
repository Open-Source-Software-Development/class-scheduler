package osd.schedule;

import osd.input.Section;
import osd.output.Hunk;

import java.util.List;

/**
 * A mutable work-in-progress schedule. Hunks may be added to the scheduler,
 * but no two hunks may have the same {@linkplain Hunk#getSection() section}.
 * Hunks may also be removed by section.
 */
interface Scheduler {

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
    boolean addHunk(final Hunk hunk);

    /**
     * Gets the hunk for some section and removes it. If no hunk existed with
     * that section, the removal fails with a {@link java.util.NoSuchElementException}.
     * @param section
     */
    void removeHunk(final Section section);

    /**
     * Gets all the hunks that have been added (and not removed). The
     * returned list supports mutation operations; changes to that list
     * will not affect the scheduler, nor vice-verse.
     * @return all the hunks in the schedule
     */
    List<Hunk> getHunks();

}
