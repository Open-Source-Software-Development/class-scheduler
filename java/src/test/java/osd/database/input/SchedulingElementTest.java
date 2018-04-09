package osd.database.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchedulingElementTest {

    private final int id = (int)(Math.random() * 1000);
    private final String name = "Louis Cypher";
    private final SchedulingElement instance = new SchedulingElement(id, name);

    private class AnotherSchedulingElement extends SchedulingElement {
        AnotherSchedulingElement(final int id, final String name) {
            super(id, name);
        }
    }

    @Test
    void getId() {
        assertEquals(id, instance.getId());
    }

    @Test
    void getName() {
        assertEquals(name, instance.getName());
    }

    @Test
    void testHashCode() {
        assertEquals(instance.getId(), instance.hashCode());
    }

    @Test
    void equals() {
        assertTrue(instance.equals(new SchedulingElement(id, name)));
    }

    @Test
    void equals_Id() {
        assertFalse(instance.equals(new SchedulingElement(id + 1, name)));
    }

    @Test
    void equals_Name() {
        // We don't actually care that the names are the same...
        assertTrue(instance.equals(new SchedulingElement(id, name + ", Esq.")));
    }

    @Test
    void equals_Class() {
        assertFalse(instance.equals(new AnotherSchedulingElement(id, name)));
    }

    @Test
    void equals_Null() {
        assertFalse(instance.equals(null));
    }

    @Test
    void testToString() {
        assertEquals(instance.getName(), instance.toString());
    }

}