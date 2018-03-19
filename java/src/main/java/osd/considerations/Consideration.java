package osd.considerations;

import osd.output.Hunk;

import java.util.function.Predicate;

interface Consideration extends Predicate<Hunk> {

    /**
     * Indicates whether we always make the same decision for the same hunk.
     * This should always be true for user considerations, and will often be
     * false for base considerations.
     * @return whether the predicate is constant per hunk
     */
    boolean isConstant();

}
