package osd.schedule;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.input.*;
import osd.output.Hunk;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AvailabilityTest {

    // Some bullshit test data.
    // We'll use the following rules for our mock constraints:
    // - For mockSection3, any hunk is valid.
    // - For mockSection1, everything else in the hunk must contain a 1.
    // - For mockSection2, everything else in the hunk must contain a 2.
    @Mock private Sources mockSources;
    @Mock private Professor mockProfessor1;
    @Mock private Professor mockProfessor2;
    @Mock private Section mockSection1;
    @Mock private Section mockSection2;
    @Mock private Section mockSection3;
    @Mock private Room mockRoom1;
    @Mock private Room mockRoom2;
    @Mock private Block mockBlockA1;
    @Mock private Block mockBlockA2;
    @Mock private Block mockBlockB1;
    @Mock private Block mockBlockB2;
    @Mock private Constraints mockConstraints;

    private Availability instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockBlockA1.getNext()).thenReturn(mockBlockA2);
        when(mockBlockA2.getPrevious()).thenReturn(mockBlockA1);
        when(mockBlockB1.getNext()).thenReturn(mockBlockB2);
        when(mockBlockB2.getPrevious()).thenReturn(mockBlockB1);
        when(mockBlockA1.getPairedWith()).thenReturn(mockBlockB1);
        when(mockBlockA2.getPairedWith()).thenReturn(mockBlockB2);
        when(mockSources.getBlocks()).thenAnswer((z) ->
                Stream.of(mockBlockA1, mockBlockA2, mockBlockB1, mockBlockB2));
        when(mockSources.getProfessors()).thenAnswer((z) ->
                Stream.of(mockProfessor1, mockProfessor2));
        when(mockSources.getRooms()).thenAnswer((z) ->
                Stream.of(mockRoom1, mockRoom2));
        when(mockSources.getSections()).thenAnswer((z) ->
                Stream.of(mockSection1, mockSection2, mockSection3));
        when(mockConstraints.test(any())).thenAnswer(a -> {
                final Hunk hunk = a.getArgument(0);
                final Section section = hunk.getSection();
                final Professor professor = hunk.getProfessor();
                final Room room = hunk.getRoom();
                final Block block = hunk.getBlock();
                if (section.equals(mockSection3)) {
                    return true;
                }
                if (section == mockSection1) {
                    return (professor == mockProfessor1 || professor == null)
                            && (room == mockRoom1 || room == null)
                            && (block == mockBlockA1 || block == mockBlockB1 || block == null);
                }
                if (section == mockSection2) {
                    return (professor == mockProfessor2 || professor == null)
                            && (room == mockRoom2 || room == null)
                            && (block == mockBlockA2 || block == mockBlockB2 || block == null);
                }
                throw new AssertionError();
            });
        instance = new Availability(mockSources, mockConstraints);
    }

    @Test
    void getImpacted() {
        // Since mockSection3 can be scheduled anywhere, it should be impacted
        // by either of the other mock sections.
        final Hunk hunk = new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockA1);
        final Set<Section> expected = Collections.singleton(mockSection3);
        final Set<Section> result = instance.getImpacted(hunk)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void getProfessors() {
        final Set<Professor> expected = Collections.singleton(mockProfessor1);
        final Set<Professor> result = instance.getProfessors(mockSection1)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void getRooms() {
        final Set<Room> expected = Collections.singleton(mockRoom1);
        final Set<Room> result = instance.getRooms(mockSection1)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void getBlocks() {
        final Set<Block> expected = new HashSet<>(Arrays.asList(
                mockBlockA1, mockBlockA2,
                mockBlockB1, mockBlockB2));
        final Set<Block> result = instance.getBlocks(mockProfessor1, mockRoom1)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void getBlocks_RespectsAddHunk_ForProfessor() {
        instance.onHunkAdded(new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockB2));
        final Set<Block> expected = new HashSet<>(Arrays.asList(
                mockBlockA1, mockBlockA2,
                mockBlockB1));
        final Set<Block> result = instance.getBlocks(mockProfessor1, mockRoom2)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void getBlocks_RespectsAddHunk_ForRoom() {
        instance.onHunkAdded(new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockB2));
        final Set<Block> expected = new HashSet<>(Arrays.asList(
                mockBlockA1, mockBlockA2,
                mockBlockB1));
        final Set<Block> result = instance.getBlocks(mockProfessor2, mockRoom1)
                .collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void candidates() {
        final Set<Hunk> expected = new HashSet<>(Arrays.asList(
                new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockA1),
                new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockA2),
                new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockB1),
                new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockB2)
        ));
        final Set<Hunk> result = instance.candidates(mockSection1).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void candidates_RespectsAddHunk() {
        instance.onHunkAdded(new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockB2));
        final Set<Hunk> expected = new HashSet<>(Arrays.asList(
                new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockA1),
                new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockA2),
                new Hunk(mockSection1, mockProfessor1, mockRoom1, mockBlockB1)
        ));
        final Set<Hunk> result = instance.candidates(mockSection1).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

}