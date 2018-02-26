package demo;

import osd.output.Hunk;
import osd.considerations.Preference;

import javax.inject.Inject;

class DemoPreferences implements Preference {

    @Inject DemoPreferences() {}

    @Override
    public Integer evaluate(final Hunk hunk) {
        // for now, score everything at 1
        return 1;
    }

}
