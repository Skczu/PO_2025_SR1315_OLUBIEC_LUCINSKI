package integration.model;

import agh.ics.oop.model.*;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GrassFieldIntegrationTest {

    @Test
    public void placesAnimalsOnlyOnAnyValidPosition(){
        //when
        Animal rabbit = new Animal(new Vector2d(0,1), List.of(1),5);
        Animal owl = new Animal(new Vector2d(0,1), List.of(1),5) ;
        Animal tigger = new Animal(new Vector2d(-1,10), List.of(1),5);
        Animal eeyore = new Animal(new Vector2d(0,0), List.of(1),5);
        GrassField map = new GrassField(5);

        //then
        assertDoesNotThrow( () ->map.place(rabbit));
        assertThrows(IncorrectPositionException.class, () ->map.place(owl));
        assertDoesNotThrow(() ->map.place(tigger));
        assertDoesNotThrow(() ->map.place(eeyore));
    }


    @Test
    public void placesAnimalsOverGrass(){
        //when
        GrassField map = new GrassField(1); //map of size 4x4 with one patch od grass in int

        //then
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int finalI = i;
                int finalJ = j;
                assertDoesNotThrow( () -> map.place(new Animal(new Vector2d(finalI, finalJ), List.of(1),5)));
            }
        }
    }


    @Test
    public void returnsWorldElementAtPositionOrNull() throws IncorrectPositionException{
        //given
        GrassField map =new GrassField(5);
        Animal kanga = new Animal(new Vector2d(2,3), List.of(1),5);

        //when
        map.place(kanga);

        //then
        assertEquals(kanga,map.objectAt(new Vector2d(2,3)));
        assertNull(map.objectAt(new Vector2d(-1,-1)));
    }

    @Test
    public void checksIfOccupied() throws IncorrectPositionException{
        //when
        GrassField map =new GrassField(5);
        map.place(new Animal(new Vector2d(1,3), List.of(1),5));

        //then
        assertTrue(map.isOccupied(new Vector2d(1,3)));
        assertFalse(map.isOccupied(new Vector2d(-1,-2)));
    }


    @Test
    public void acceptsValidRejectsInvalidMoves() throws IncorrectPositionException{
        //when
        GrassField map =new GrassField(5);
        map.place(new Animal(new Vector2d(1,3), List.of(1),5));

        //then
        assertTrue(map.canMoveTo(new Vector2d(2,3)));
        assertFalse(map.canMoveTo(new Vector2d(1,3)));
        assertTrue(map.canMoveTo(new Vector2d(-1,-1)));
        assertTrue(map.canMoveTo(new Vector2d(1,5)));
    }


    @Test
    public void movesCorrectly() throws IncorrectPositionException{
        //given
        GrassField map = new GrassField(5);
        Animal pooh = new Animal(new Vector2d(1,1), List.of(1),5);
        Animal piglet = new Animal(new Vector2d(2,2), List.of(1),5);

        //when
        map.place(pooh);
        map.place(piglet);

        map.move(pooh, MoveDirection.RIGHT);
        map.move(pooh, MoveDirection.FORWARD);
        map.move(piglet, MoveDirection.LEFT);
        map.move(piglet, MoveDirection.BACKWARD);
        map.move(piglet, MoveDirection.LEFT);
        map.move(piglet, MoveDirection.FORWARD);
        Map<Vector2d, Animal> mapAfterMoves =  map.getAnimals();

        //then
        assertTrue(pooh.isAt(new Vector2d(2,1)));
        assertEquals(MapDirection.EAST,pooh.getCurrentOrientation());
        assertEquals(pooh,mapAfterMoves.get(new Vector2d(2,1)));

        assertTrue(piglet.isAt(new Vector2d(3,1)));
        assertEquals(MapDirection.SOUTH,piglet.getCurrentOrientation());
        assertEquals(piglet,mapAfterMoves.get(new Vector2d(3,1)));
    }

    @Test
    public void discardsInvalidMoves() throws IncorrectPositionException{
        //given
        GrassField map = new GrassField(5);
        Animal piglet = new Animal(new Vector2d(0,0), List.of(1),5);
        Animal tigger = new Animal(new Vector2d(0,1), List.of(1),5);

        //when
        map.place(piglet);
        map.place(tigger);
        map.move(tigger, MoveDirection.BACKWARD);
        map.move(piglet, MoveDirection.BACKWARD);
        Map<Vector2d, Animal> mapAfterMoves =  map.getAnimals();

        //then
        assertTrue(piglet.isAt(new Vector2d(0,-1)));
        assertEquals(piglet,mapAfterMoves.get(new Vector2d(0,-1)));

        assertTrue(tigger.isAt(new Vector2d(0,1)));
        assertEquals(tigger,mapAfterMoves.get(new Vector2d(0,1)));
    }


    @Test
    public void getsCorrectElements() throws IncorrectPositionException{
        //given
        GrassField map = new GrassField(5);
        Animal pooh = new Animal(new Vector2d(4,4), List.of(1),5);
        Animal piglet = new Animal(new Vector2d(0,0), List.of(1),5);
        Animal tigger = new Animal(new Vector2d(0,1), List.of(1),5);

        //when
        map.place(pooh);
        map.place(piglet);
        map.place(tigger);
        List<WorldElement> animals= map.getElements();

        //then
        assertTrue(animals.contains(pooh));
        assertTrue(animals.contains(piglet));
        assertTrue(animals.contains(tigger));
    }
}
