package osd.main;

import osd.schedule.Callbacks;
import osd.schedule.Hunk;
import osd.schedule.Results;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Consumer;

class SchedulingCallbacks implements Callbacks {

    private final Consumer<List<Hunk>> onComplete;

    @Inject
    SchedulingCallbacks(final CompleteScheduleHandler onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    public boolean onCompleteResult(final Results results) {
        onComplete.accept(results.getHunks());
        return true;
    }

    @Override
    public boolean onBacktrack(final Results results) {
        return false;
    }

    @Override
    public void onSchedulingFailed() { }

}
