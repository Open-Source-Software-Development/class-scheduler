package demo;

import osd.considerations.Constraint;
import osd.output.Hunk;

import javax.inject.Inject;

class DemoConstraints implements Constraint {

    @Inject DemoConstraints() {}

    @Override
    public Boolean evaluate(final Hunk hunk) {
        // accepts everything
        return true;
    }
}
