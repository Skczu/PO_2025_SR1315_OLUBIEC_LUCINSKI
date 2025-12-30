package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void nextDirection() {
        //discarded given when then structure because of tested cases simplicity
        assertEquals(MapDirection.NORTHEAST,MapDirection.NORTH.next());
        assertEquals(MapDirection.EAST,MapDirection.NORTHEAST.next());
        assertEquals(MapDirection.SOUTHEAST,MapDirection.EAST.next());
        assertEquals(MapDirection.SOUTHWEST,MapDirection.SOUTH.next());
        assertEquals(MapDirection.WEST,MapDirection.SOUTHWEST.next());
        assertEquals(MapDirection.NORTHWEST,MapDirection.WEST.next());
        assertEquals(MapDirection.NORTH,MapDirection.NORTHWEST.next());
    }

}