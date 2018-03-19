package osd.considerations;

import osd.output.Hunk;

import java.util.function.Predicate;

public interface Preference extends Consideration {

    default int evaluate(final Hunk hunk) {
        return test(hunk) ? worth() : 0;
    }

    int worth();

}
