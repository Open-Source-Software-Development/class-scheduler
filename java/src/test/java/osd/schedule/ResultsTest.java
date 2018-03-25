package osd.schedule;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultsTest {

    private final List<Hunk> hunks = Arrays.asList(null, null, null);
    private final int expectedHunkCount = 7;

    private Results instance = new Results() {

        @Override
        public List<Hunk> getHunks() {
            return hunks;
        }

        @Override
        public int getExpectedHunkCount() {
            return expectedHunkCount;
        }

    };

    @Test
    void getHunkPercentage() {
        final double expected = (double)hunks.size() / (double)expectedHunkCount;
        final double result = instance.getHunkPercentage();
        assertEquals(expected, result);
    }

}