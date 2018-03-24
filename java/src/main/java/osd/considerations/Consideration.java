package osd.considerations;

import osd.schedule.Hunk;

import java.util.function.Predicate;

/**
 * Superinterface for preferences and constraints. When you see "consideration"
 * in the docs, read it as "preference" or "constraint" as appropriate.
 */
interface Consideration extends Predicate<Hunk> {
}
