package osd.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BlockingStrategyTest {

    @Mock private Block mockBlockA1;
    @Mock private Block mockBlockA2;
    @Mock private Block mockBlockB1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockBlockA1.getNext()).thenReturn(mockBlockB1);
        when(mockBlockA1.getPairedWith()).thenReturn(mockBlockA2);
    }

    @Test
    void pair() {
        final Set<Block> expected = new HashSet<>(Arrays.asList(mockBlockA1, mockBlockA2));
        final Set<Block> result = BlockingStrategy.PAIR.apply(mockBlockA1).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void sequential() {
        final Set<Block> expected = new HashSet<>(Arrays.asList(mockBlockA1, mockBlockB1));
        final Set<Block> result = BlockingStrategy.SEQUENTIAL.apply(mockBlockA1).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

}