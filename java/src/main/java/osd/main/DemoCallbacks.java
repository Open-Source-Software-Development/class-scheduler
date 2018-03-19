package osd.main;

import osd.output.Callbacks;
import osd.output.Results;

class DemoCallbacks implements Callbacks {

    private final int desiredResultCount;
    private int resultCount = 0;
    private int backtracks = 0;

    DemoCallbacks(final int desiredResultCount) {
        this.desiredResultCount = desiredResultCount;
    }

    @Override
    public boolean stopCondition() {
        return resultCount >= desiredResultCount;
    }

    @Override
    public void onCompleteResult(final Results results) {
        results.getHunks().forEach(System.out::println);
        resultCount++;
        if (stopCondition()) {
            System.out.println("Completed with " + backtracks + " backtracks");
        }
    }

    @Override
    public void onBacktrack(final Results results) {
        backtracks++;
    }

}
