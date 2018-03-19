package osd.schedule;

import osd.output.Hunk;
import osd.output.Results;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class SchedulerResults implements Results {

    private final SchedulerLookups lookups;
    private final int expectedCount;

    SchedulerResults(final int expectedCount, final SchedulerLookups lookups) {
        this.lookups = lookups;
        this.expectedCount = expectedCount;
    }

    @Override
    public List<Hunk> getHunks() {
        return new ArrayList<>(lookups.lookupAllHunks().collect(Collectors.toList()));
    }

    @Override
    public int getExpectedHunkCount() {
        return expectedCount;
    }
}
