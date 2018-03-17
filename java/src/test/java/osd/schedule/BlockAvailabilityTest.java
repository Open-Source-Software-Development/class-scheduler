package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.input.Block;
import osd.input.Professor;
import osd.input.Room;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BlockAvailabilityTest {

    @Mock private Block mockBlock;
    @Mock private Room mockRoom;
    @Mock private Professor mockProfessor;
    private BlockAvailability instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        instance = new BlockAvailability(Collections.singleton(mockBlock));
    }

    @Test
    void getAvailable() {
        final Set<Block> expected = Collections.singleton(mockBlock);
        final Set<Block> result = instance.getAvailable(mockProfessor, mockRoom)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void setUnavailable_Room() {
        instance.setUnavailable(mockBlock, mockRoom);
        final Set<Block> expected = Collections.emptySet();
        final Set<Block> result = instance.getAvailable(mockProfessor, mockRoom)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void setUnavailable_Professor() {
        instance.setUnavailable(mockBlock, mockProfessor);
        final Set<Block> expected = Collections.emptySet();
        final Set<Block> result = instance.getAvailable(mockProfessor, mockRoom)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

}