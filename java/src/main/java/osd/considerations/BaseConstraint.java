package osd.considerations;

import osd.schedule.Lookups;

public interface BaseConstraint extends BaseConsideration<Constraint> {

    @Override
    default Constraint bind(final Lookups lookups) {
        return bindPredicate(lookups)::test;
    }

}
