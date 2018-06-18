package osd.schedule;

import java.util.List;

public interface Results {

    /**
     * Get the list of all hunks generated so far. This list may be safely
     * modified; modifications will not be reflected in the scheduler nor
     * future calls to this method.
     * @return the list of all hunks generated so far
     */
    List<Hunk> getHunks();

    /**
     * Get the count of how many hunks we expect to have.
     * @return the count of how many hunks we expect to have
     */
    int getExpectedHunkCount();

    /**
     * Compute the percentage of hunks generated successfully. The percentage
     * is normalized such that 1 represents 100%.
     * @return the percentage of hunks generated successfully
     */
    default double getHunkPercentage() {
        return (double)(getHunks().size()) / (double)getExpectedHunkCount();
    }

}
