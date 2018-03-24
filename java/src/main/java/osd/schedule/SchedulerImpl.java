package osd.schedule;

import javax.inject.Inject;

class SchedulerImpl implements Scheduler {

    private final SchedulerCandidate initialGeneration;
    private final Callbacks callbacks;

    /**
     * DI constructor. Constructs a scheduler that fills in data for the
     * supplied (blank) initial schedule.
     * @param initialGeneration a blank candidate schedule to fill in
     */
    @Inject
    SchedulerImpl(final SchedulerCandidate initialGeneration, final Callbacks callbacks) {
        this.initialGeneration = initialGeneration;
        this.callbacks = callbacks;
    }

    @Override
    public void run() {
        boolean succeeded = run0(initialGeneration);
        if (!succeeded) {
            callbacks.onSchedulingFailed();
        }
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
