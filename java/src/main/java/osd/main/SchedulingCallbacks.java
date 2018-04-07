package osd.main;

import osd.schedule.Callbacks;
import osd.schedule.Results;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class SchedulingCallbacks implements Callbacks {

    private final Consumer<Results> onComplete;
    private final List<Results> completeResults = new ArrayList<>();

    @Inject
    SchedulingCallbacks(final CompleteScheduleHandler onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    public boolean onCompleteResult(final Results results) {
        completeResults.add(results);
        return true;
    }

    @Override
    public boolean onBacktrack(final Results results) {
        return false;
    }

    @Override
    public void done(final boolean success) {
        completeResults.forEach(onComplete);
    }

}
