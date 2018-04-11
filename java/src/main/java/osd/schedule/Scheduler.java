package osd.schedule;

import javax.inject.Inject;

/**
 * Schedule algorithm entry point.
 */
public class Scheduler implements Runnable {

    private final SchedulerCandidate initialGeneration;
    private final Callbacks callbacks;

    /**
     * DI constructor. Constructs a scheduler that fills in data for the
     * supplied (blank) initial schedule.
     * @param initialGeneration a blank candidate schedule to fill in
     */
    @Inject
    Scheduler(final SchedulerCandidate initialGeneration, final Callbacks callbacks) {
        this.initialGeneration = initialGeneration;
        this.callbacks = callbacks;
    }

    @Override
    public void run() {
        final boolean success = run0(initialGeneration);
        callbacks.done(success);
    }

    private boolean run0(final SchedulerCandidate generation) {
        if (generation.isComplete()) {
            return callbacks.onCompleteResult(generation);
        }
        if (generation.isImpossible()) {
            return callbacks.onBacktrack(generation);
        }
        // Look through the next generation, in preference order,
        // and recurse into them.
        return generation.getNextGenerationCandidates()
                .anyMatch(this::run0);
    }

}
