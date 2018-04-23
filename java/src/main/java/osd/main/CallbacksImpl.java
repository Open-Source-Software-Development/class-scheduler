package osd.main;

import osd.schedule.Callbacks;
import osd.schedule.Results;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Controls the scheduling algorithm. In particular, this class decides what
 * schedules should get written to the database, and when the algorithm should
 * stop. Any changes, eg. writing multiple schedules, to those policies should
 * go here.
 * @see Callbacks
 */
class CallbacksImpl implements Callbacks {

    private final CompleteScheduleHandler onComplete;
    // This is a List because we expect to generate multiple schedules
    // in the future, even though it only ever holds one right now.
    private final List<Results> completeResults = new ArrayList<>();

    @Inject
    CallbacksImpl(final CompleteScheduleHandler onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    public boolean onCompleteResult(final Results results) {
        // TODO: we want to give the registrar a couple choices
        completeResults.add(results);
        return true;
    }

    @Override
    public boolean onBacktrack(final Results results) {
        return false;
    }

    @Override
    public void done(final boolean success) {
        for (final Results results: completeResults) {
            onComplete.accept(results);
        }
    }

}
