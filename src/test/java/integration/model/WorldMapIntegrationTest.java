package integration.model;

import agh.ics.oop.model.*;
import agh.ics.oop.model.enums.SimulationParameters;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WorldMapIntegrationTest {

    static final SimulationParameters USED_PARAMS = new SimulationParameters(10, 10, true, 4, 10, 2, 12, 3, 0, 10, 4, 2, 5, 1, 2, 3, 4);

    @Test
    void placesAnimalsOnlyOnValidPositions() {
        //when
        Animal rabbit = new Animal(new Vector2d(0, 1), new Genome(List.of(1)), 5);
        Animal owl = new Animal(new Vector2d(0, 1), new Genome(List.of(1)), 5);
        Animal tigger = new Animal(new Vector2d(-1, 10), new Genome(List.of(1)), 5);
        Animal eeyore = new Animal(new Vector2d(0, 0), new Genome(List.of(1)), 5);
        WorldMap map = new WorldMap(USED_PARAMS);

        //then
        assertDoesNotThrow(() -> map.place(rabbit));
        assertDoesNotThrow(() -> map.place(owl));
        assertThrows(IncorrectPositionException.class, () -> map.place(tigger));
        assertDoesNotThrow(() -> map.place(eeyore));
    }


    @Test
    void returnsAnimalAtPosition() throws IncorrectPositionException {
        //given
        WorldMap map = new WorldMap(USED_PARAMS);
        Animal kanga = new Animal(new Vector2d(2, 3), new Genome(List.of(1)), 5);

        //when
        map.place(kanga);

        //then
        assertEquals(kanga, map.objectAt(new Vector2d(2, 3)));
        assertNull(map.objectAt(new Vector2d(0, 1))); //no grass because parameter is 0
    }

    @Test
    void checksIfOccupied() throws IncorrectPositionException {
        //when
        WorldMap map = new WorldMap(USED_PARAMS);
        map.place(new Animal(new Vector2d(1, 3), new Genome(List.of(1)), 5));

        //then
        assertTrue(map.isOccupied(new Vector2d(1, 3)));
        assertFalse(map.isOccupied(new Vector2d(1, 2)));
    }


    @Test
    void copulateCorrectly() throws IncorrectPositionException{
        //given
        WorldMap map = new WorldMap(USED_PARAMS);
        Animal animal1 = new Animal(new Vector2d(4, 0), new Genome(List.of(7)), 150);
        Animal animal2 = new Animal(new Vector2d(4, 0), new Genome(List.of(4)), 150);
        Animal animal3 = new Animal(new Vector2d(4, 0), new Genome(List.of(4)), 1);
        Animal animal4 = new Animal(new Vector2d(4, 0), new Genome(List.of(4)), 1);

        //when
        map.place(animal1);
        map.place(animal2);
        map.place(animal3);
        map.place(animal4);
        map.copulate();

        //then
        Map<Vector2d, List<Animal>> afterCopulation = map.getAnimals();
        assertEquals(5,afterCopulation.get(animal1.getPosition()).size());
    }


    @Test
    void removesDead() throws IncorrectPositionException{
        //given
        WorldMap map = new WorldMap(USED_PARAMS);
        Animal animal1 = new Animal(new Vector2d(4, 0), new Genome(List.of(7)), 150);
        Animal animal2 = new Animal(new Vector2d(4, 1), new Genome(List.of(4)), -1);
        Animal animal3 = new Animal(new Vector2d(4, 1), new Genome(List.of(4)), 0);
        Animal animal4 = new Animal(new Vector2d(4, 4), new Genome(List.of(4)), -1);

        //when
        map.place(animal1);
        map.place(animal2);
        map.place(animal3);
        map.place(animal4);
        map.removeDeadAnimals();

        //then
        Map<Vector2d, List<Animal>> afterCopulation = map.getAnimals();
        assertEquals(1,afterCopulation.get(animal1.getPosition()).size());
    }

    @Test
    void eatsGrass() throws IncorrectPositionException{
        //given
        WorldMap map = new WorldMap(USED_PARAMS);
        Animal eeyore = new Animal(new Vector2d(4, 0), new Genome(List.of(7)), 150);
        Animal pooh = new Animal(new Vector2d(4, 0),new Genome(List.of(7)), 15);
        map.getGrasses().put(new Vector2d(4,0),new Grass(new Vector2d(4,0)));

        //when
        map.place(eeyore);
        map.place(pooh);
        map.consumeGrass();

        //then
        assertEquals(154,eeyore.getEnergy());
        assertEquals(15,pooh.getEnergy());
    }

    @Test
    void movesCorrectly() throws IncorrectPositionException {
        //given
        WorldMap map = new WorldMap(USED_PARAMS);
        Animal pooh = new Animal(new Vector2d(4, 0), new Genome(List.of(7)), 5);
        Animal piglet = new Animal(new Vector2d(0, 0), new Genome(List.of(4)), 5);
        Animal tigger = new Animal(new Vector2d(9, 8), new Genome(List.of(2)), 5);
        Animal rabbit = new Animal(new Vector2d(8, 9), new Genome(List.of(1)), 5);
        Animal owl = new Animal(new Vector2d(9, 0), new Genome(List.of(3)), 5);

        //when
        map.place(pooh);
        map.place(piglet);
        map.place(tigger);
        map.place(rabbit);
        map.place(owl);

        pooh.rotate();
        piglet.rotate();
        tigger.rotate();
        rabbit.rotate();
        owl.rotate();

        map.regularMove(pooh);
        map.regularMove(piglet);
        map.regularMove(tigger);
        map.regularMove(rabbit);
        map.regularMove(owl);

        //then
        assertTrue(pooh.isAt(new Vector2d(3, 1)));
        assertTrue(piglet.isAt(new Vector2d(0, 1)));
        assertTrue(tigger.isAt(new Vector2d(0, 8)));
        assertTrue(rabbit.isAt(new Vector2d(9, 8)));
        assertTrue(owl.isAt(new Vector2d(0, 1)));
    }


    @Test
    void movesFastCorrectly() throws IncorrectPositionException{
        WorldMap map = new WorldMap(USED_PARAMS);
        Animal tigger = new Animal(new Vector2d(4, 0), new Genome(List.of(0)), 10);
        Animal piglet = new Animal(new Vector2d(5, 1),new Genome( List.of(2)), 1);
        Animal eeyore = new Animal(new Vector2d(3, 1),new Genome( List.of(4)), 1);
        map.getGrasses().put(new Vector2d(4,1),new Grass(new Vector2d(4,0)));

        //when
        map.place(tigger);
        map.place(piglet);
        map.place(eeyore);
        piglet.rotate();
        tigger.rotate();
        eeyore.rotate();

        map.fastMove(tigger);
        map.fastMove(piglet);
        map.fastMove(eeyore);

        //then
        assertEquals(new Vector2d(4,4),tigger.getPosition());
        assertEquals(8,tigger.getEnergy());
        assertEquals(new Vector2d(6,1),piglet.getPosition());
        assertEquals(-1,piglet.getEnergy());
        assertEquals(new Vector2d(3,0),eeyore.getPosition());
        assertEquals(-1,eeyore.getEnergy());
    }

    @Test
    void collidesCorrectly() throws IncorrectPositionException{
        //given
        WorldMap map = new WorldMap(USED_PARAMS);
        Animal tigger = new Animal(new Vector2d(4, 0), new Genome(List.of(0)), 150);
        Animal piglet = new Animal(new Vector2d(5, 1),new Genome( List.of(6)), 3);
        map.getGrasses().put(new Vector2d(4,1),new Grass(new Vector2d(4,0)));

        //when
        map.place(tigger);
        map.place(piglet);
        piglet.rotate();
        tigger.rotate();

        map.regularMove(piglet);
        map.fastMove(tigger);


        //then collision with piglet
        assertEquals(new Vector2d(4,1),tigger.getPosition());
        assertEquals(146,tigger.getEnergy());
        assertEquals(new Vector2d(4,1),piglet.getPosition());
    }

    @Test
    void movesAccordingToGenome() throws IncorrectPositionException{
        WorldMap map = new WorldMap(USED_PARAMS);
        Animal tigger = new Animal(new Vector2d(4, 0), new Genome(List.of(0,1)), 150);


        //when
        map.place(tigger);

        tigger.rotate();
        map.regularMove(tigger);

        tigger.rotate();
        map.regularMove(tigger);

        tigger.rotate();
        map.regularMove(tigger);

        //then
        assertEquals(new Vector2d(6,3),tigger.getPosition());
    }

    @Test
    void growsGrassIfPossible() throws IncorrectPositionException{
        //given
        WorldMap map = new WorldMap(USED_PARAMS);
        Animal tigger = new Animal(new Vector2d(4, 0), new Genome(List.of(0)), 150);
        Animal piglet = new Animal(new Vector2d(5, 1), new Genome(List.of(6)), 3);
        map.getGrasses().put(new Vector2d(4,0),new Grass(new Vector2d(4,0)));

        //when
        map.place(tigger);
        map.place(piglet);
        map.growGrass(100);
        map.growGrass(100);

        //then
        assertEquals(100,map.getGrasses().size());
    }
}
