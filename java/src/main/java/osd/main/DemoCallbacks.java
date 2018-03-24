package osd.main;

import osd.schedule.Callbacks;
import osd.schedule.Results;

import javax.inject.Inject;

class DemoCallbacks implements Callbacks {

    private int backtracks = 0;

    @Inject
    DemoCallbacks() {
    }

    @Override
    public boolean onCompleteResult(final Results results) {
        System.err.flush();
        System.out.println("Received candidate schedule:");
        results.getHunks().forEach(System.out::println);
        System.out.println("Completed with " + backtracks + " backtracks");
        return true;
    }

    @Override
    public boolean onBacktrack(final Results results) {
        backtracks++;
        return false;
    }

    @Override
    public void onSchedulingFailed() {
        System.out.println("Out of candidates with " + backtracks + " backtracks");
    }

}
