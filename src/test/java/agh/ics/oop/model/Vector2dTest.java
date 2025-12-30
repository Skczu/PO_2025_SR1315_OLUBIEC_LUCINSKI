package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void isEqualVectors(){ //does not check pointers to the same object (always same value)
        //when
        Vector2d a = new Vector2d(1,0);
        Vector2d b = new Vector2d(1,0);
        //then
        assertTrue(a.equals(b));
    }

    @Test
    void nonEqualVectors(){
        //when
        Vector2d a = new Vector2d(1,0);
        Vector2d b = new Vector2d(0,1);
        String c = "(1,0)"; //case for different object type
        //then
        assertFalse(a.equals(b));
        assertFalse(a.equals(c));
    }

    @Test
    void correctString(){
        //when
        Vector2d a = new Vector2d(1,2);
        //then
        assertEquals("(1,2)",a.toString());
    }

    @Test
    void vectorPrecedes(){
        //when
        Vector2d a = new Vector2d(1,0);
        Vector2d b = new Vector2d(2,1);
        //then
        assertTrue(a.precedes(b));
        assertTrue(a.precedes(a)); //vector precedes itself
        assertFalse(b.precedes(a));
    }

    @Test
    void vectorFollows(){
        //when
        Vector2d a = new Vector2d(2,1);
        Vector2d b = new Vector2d(1,0);
        //then
        assertTrue(a.follows(b));
        assertTrue(a.follows(a)); //vector follows itself
        assertFalse(b.follows(a));
    }

    @Test
    void isMaxUpperRight(){
        //when
        Vector2d a = new Vector2d(2,0);
        Vector2d b = new Vector2d(-1,1);
        //then
        assertEquals(new Vector2d(2,1),a.upperRight(b));
    }

    @Test
    void isMinLowerLeft(){
        //when
        Vector2d a = new Vector2d(2,0);
        Vector2d b = new Vector2d(-1,1);
        //then
        assertEquals(new Vector2d(-1,0),a.lowerLeft(b));
    }

    @Test
    void addVectors(){
        //when
        Vector2d a = new Vector2d(2,2);
        Vector2d b = new Vector2d(-1,1);
        //then
        assertEquals(new Vector2d(1,3),a.add(b));
    }

    @Test
    void subtractVectors(){
        //when
        Vector2d a = new Vector2d(2,3);
        Vector2d b = new Vector2d(-1,1);
        //then
        assertEquals(new Vector2d(3,2),a.subtract(b));
    }

    @Test
    void oppositeVector() {
        //when
        Vector2d a = new Vector2d(-1, 2);
        //then
        assertEquals(new Vector2d(1, -2), a.opposite());
    }
}