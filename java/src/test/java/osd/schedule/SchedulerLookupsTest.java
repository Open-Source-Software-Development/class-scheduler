package osd.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.input.Block;
import osd.input.Professor;
import osd.input.Room;
import osd.input.Section;
import osd.output.Hunk;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerLookupsTest {

    @Mock private Section mockSection;
    @Mock private Professor mockProfessor;
    @Mock private Block mockBlock;
    @Mock private Room mockRoom;
    private Hunk hunk;
    private SchedulerLookups instance = SchedulerLookups.empty();

    @Mock private Section differentMockSection;
    @Mock private Professor differentMockProfessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        hunk = new Hunk(mockSection, mockProfessor, mockRoom, Collections.singletonList(mockBlock));
        instance = instance.extend(hunk);
    }

    @Test
    void lookupAllHunks() {
        final Set<Hunk> expected = Collections.singleton(hunk);
        final Set<Hunk> result = instance.lookupAllHunks().collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void lookup_ByProfessor() {
        final Set<Hunk> expected = Collections.singleton(hunk);
        final Set<Hunk> result = instance.lookup(mockProfessor).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void lookup_BySection() {
        assertEquals(hunk, instance.lookup(mockSection));
    }


    @Test
    void lookup_ByProfessor_EmptyOnAbsent() {
        assertEquals(0, instance.lookup(differentMockProfessor).count());
    }

    @Test
    void lookup_BySection_NullOnAbsent() {
        assertNull(instance.lookup(differentMockSection));
    }

}