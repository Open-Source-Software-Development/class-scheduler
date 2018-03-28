package osd.flags;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlagsTest {

    private static final String HOST = "host";
    private static final String NAME = "name";
    private static final String USER = "user";
    private static final String PASS = "pass";
    private static final String[] COMMAND_LINE = {
            "--dbHost", "host",
            "--dbName", "name",
            "--dbUser", "user",
            "--dbPass", "pass",
    };

    private Flags instance;

    @BeforeEach
    void setUp() throws ParseException {
        instance = new FlagModule(COMMAND_LINE).providesFlags();
    }

    @Test
    void getDatabaseHost() {
        assertEquals(HOST, instance.getDatabaseHost());
    }

    @Test
    void getDatabaseName() {
        assertEquals(NAME, instance.getDatabaseName());
    }

    @Test
    void getDatabaseUser() {
        assertEquals(USER, instance.getDatabaseUser());
    }

    @Test
    void getDatabasePass() {
        assertEquals(PASS, instance.getDatabasePass());
    }

}