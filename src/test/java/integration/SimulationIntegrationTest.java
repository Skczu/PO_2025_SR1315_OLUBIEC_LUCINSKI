package integration;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.enums.SimulationParameters;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

class SimulationIntegrationTest {

    static final SimulationParameters USED_PARAMS1 = new SimulationParameters(10, 10, true, 0, 10, 2, 12, 3, 0, 10, 4, 2, 5, 1, 2, 3, 4);
    static final SimulationParameters USED_PARAMS2 = new SimulationParameters(10, 10, true, 125, 10, 2, 12, 3, 125, 10, 4, 2, 5, 1, 2, 3, 4);
    static final SimulationParameters USED_PARAMS3 = new SimulationParameters(10, 10, true, 125, 0, 2, 12, 3, 0, 0, 4, 2, 5, 1, 2, 3, 4);


    @Test
    public void animalsMoveCorrectly() throws IncorrectPositionException {
        //given
        WorldMap map = new WorldMap(USED_PARAMS1);
        Simulation sim = new Simulation(map,USED_PARAMS1);
        Animal pooh = new Animal(new Vector2d(4, 0), List.of(7), 5);
        Animal piglet = new Animal(new Vector2d(0, 0), List.of(4), 5);
        Animal tigger = new Animal(new Vector2d(9, 8), List.of(2), 5);
        Animal rabbit = new Animal(new Vector2d(8, 9), List.of(1), 5);
        Animal owl = new Animal(new Vector2d(9, 0), List.of(3), 5);


        //when
        map.place(pooh);
        sim.getAnimals().add(pooh);
        map.place(piglet);
        sim.getAnimals().add(piglet);
        map.place(tigger);
        sim.getAnimals().add(tigger);
        map.place(rabbit);
        sim.getAnimals().add(rabbit);
        map.place(owl);
        sim.getAnimals().add(owl);
        sim.runOneDay();


        //then
        System.out.println(pooh.getPosition());
        assertTrue(pooh.isAt(new Vector2d(3, 1)));
        assertTrue(piglet.isAt(new Vector2d(0, 1)));
        assertTrue(tigger.isAt(new Vector2d(0, 8)));
        assertTrue(rabbit.isAt(new Vector2d(9, 8)));
        assertTrue(owl.isAt(new Vector2d(0, 1)));
    }



    @Test
    public void animalsAndGrassCanOverlap(){
        //given
        WorldMap map = new WorldMap(USED_PARAMS2);
        Simulation sim = new Simulation(map,USED_PARAMS2);

        //when
        List<Animal> animalsAfterSim= sim.getAnimals();
        Map<Vector2d, Grass> grasses = map.getGrasses();

        //then
        assertEquals(125,animalsAfterSim.size());
        assertEquals(100,grasses.size());
    }


    @Test
    public void removesDeadAnimals(){
        //given
        WorldMap map1 = new WorldMap(USED_PARAMS3);
        Simulation sim1 = new Simulation(map1,USED_PARAMS3);

        WorldMap map2 = new WorldMap(USED_PARAMS2);
        Simulation sim2 = new Simulation(map2,USED_PARAMS2);

        //when
        sim1.runOneDay();
        sim1.runOneDay();
        sim2.runOneDay();
        sim2.runOneDay();

        //then
        assertTrue(sim1.getAnimals().isEmpty());
        assertFalse(sim2.getAnimals().isEmpty());
    }

}
