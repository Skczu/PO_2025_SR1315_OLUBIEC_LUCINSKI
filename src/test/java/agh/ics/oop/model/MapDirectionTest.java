package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {

    @Test
    void transformsDigitsCorrectly(){
        //when
        int a = 0;
        int b = 7;
        int c = 8;
        int d = 1001;
        int e = -2;

        //then
        assertEquals(MapDirection.NORTH,MapDirection.fromDigit(a));
        assertEquals(MapDirection.NORTHWEST,MapDirection.fromDigit(b));
        assertEquals(MapDirection.NORTH,MapDirection.fromDigit(c));
        assertEquals(MapDirection.NORTHEAST,MapDirection.fromDigit(d));
        assertEquals(MapDirection.WEST,MapDirection.fromDigit(e));
    }

    @Test
    void rotatesCorrectly(){
        //when
        MapDirection dir1= MapDirection.NORTH;
        MapDirection dir2= MapDirection.SOUTH;
        MapDirection dir3= MapDirection.WEST;
        MapDirection rotation1 = MapDirection.SOUTH;
        MapDirection rotation2 = MapDirection.EAST;
        MapDirection rotation3 = MapDirection.SOUTHEAST;

        //then
        assertEquals(MapDirection.SOUTH,dir1.rotate(rotation1));
        assertEquals(MapDirection.WEST,dir2.rotate(rotation2));
        assertEquals(MapDirection.NORTHEAST,dir3.rotate(rotation3));
    }
}