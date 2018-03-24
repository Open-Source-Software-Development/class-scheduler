package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.Block;
import osd.database.Professor;
import osd.database.Room;
import osd.database.Section;
import osd.output.Hunk;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BlockAvailabilityTest {

    @Mock private Section mockSection;
    @Mock private Block mockBlockA, mockBlockB;
    @Mock private Room mockRoom;
    @Mock private Professor mockProfessor;

    private Hunk hunk;
    private BlockAvailability instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        hunk = new Hunk(mockSection, mockProfessor, mockRoom, Arrays.asList(mockBlockA, mockBlockB));
        instance = new BlockAvailability(Arrays.asList(mockBlockA, mockBlockB));
    }

    @Test
    void getAvailable() {
        final Set<Block> expected = new HashSet<>(Arrays.asList(mockBlockA, mockBlockB));
        final Set<Block> result = instance.getAvailable(mockProfessor, mockRoom)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void setUnavailable_Room() {
        instance.setUnavailable(hunk);
        final Set<Block> expected = Collections.emptySet();
        final Set<Block> result = instance.getAvailable(mockProfessor, mockRoom)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void setUnavailable_Professor() {
        instance.setUnavailable(hunk);
        final Set<Block> expected = Collections.emptySet();
        final Set<Block> result = instance.getAvailable(mockProfessor, mockRoom)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

}