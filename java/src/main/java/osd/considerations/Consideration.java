package osd.considerations;

import osd.output.Hunk;

import java.util.function.Predicate;

/**
 * Superinterface for preferences and constraints. When you see "consideration"
 * in the docs, read it as "preference" or "constraint" as appropriate.
 */
public interface Consideration extends Predicate<Hunk> {
}
