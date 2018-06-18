package osd.database.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import osd.database.RecordAccession;
import osd.database.input.record.RoomTypeRecord;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RoomTypeTest {

    private final int id = (int)(Math.random() * 1000);
    private final String name = "Hell";

    @Mock private RoomTypeRecord mockRecord;
    @Mock private RecordAccession mockRecordAccession;

    private RoomType instance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockRecord.getId()).thenReturn(id);
        when(mockRecord.getName()).thenReturn(name);
        instance = new RoomType(mockRecord, mockRecordAccession);
    }

    @Test
    void getId() {
        assertEquals(id, instance.getId());
    }

    @Test
    void getName() {
        assertEquals(name, instance.getName());
    }

}