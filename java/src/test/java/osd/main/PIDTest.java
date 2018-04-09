package osd.main;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PIDTest {

    @Test
    void pid() {
        // Not much to do besides make sure we don't get crash
        // and get a positive result.
        assertTrue(new PID().pid() > 0);
    }

}