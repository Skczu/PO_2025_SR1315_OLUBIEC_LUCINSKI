package integration;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static agh.ics.oop.OptionsParser.parse;
import static org.junit.jupiter.api.Assertions.*;

class SimulationIntegrationTest {

    @Test
    public void animalsHaveCorrectOrientation(){
        //given
        List<Vector2d> startPositions= List.of(new Vector2d(2,2),new Vector2d(1,3));
        List<MoveDirection> moves = List.of(MoveDirection.FORWARD, MoveDirection.BACKWARD,MoveDirection.LEFT,MoveDirection.RIGHT);
        Simulation sim = new Simulation(startPositions, moves,new RectangularMap(5,5));

        //when
        sim.run();
        List<Animal> animalsAfterSim= sim.getAnimals();

        //then
        assertEquals(MapDirection.WEST,animalsAfterSim.getFirst().getCurrentOrientation());
        assertEquals(MapDirection.EAST,animalsAfterSim.getLast().getCurrentOrientation());
    }


    @Test
    public void animalsMoveCorrectly(){
        //given
        List<Vector2d> startPositions= List.of(new Vector2d(2,2),new Vector2d(1,3));
        List<MoveDirection> moves = List.of(MoveDirection.LEFT,MoveDirection.RIGHT,MoveDirection.FORWARD,MoveDirection.BACKWARD);
        Simulation sim = new Simulation(startPositions, moves,new RectangularMap(5,5));

        //when
        sim.run();
        List<Animal> animalsAfterSim= sim.getAnimals();

        //then
        assertTrue(animalsAfterSim.getFirst().isAt(new Vector2d(1,2)));
        assertTrue(animalsAfterSim.getLast().isAt(new Vector2d(0,3)));
    }


    @Test
    public void animalsDoNotLeaveMap(){
        //given
        List<Vector2d> startPositions= List.of(new Vector2d(4,4),new Vector2d(0,0));
        List<MoveDirection> moves = List.of(MoveDirection.FORWARD,MoveDirection.BACKWARD);
        Simulation sim = new Simulation(startPositions, moves,new RectangularMap(5,5));

        //when
        sim.run();
        List<Animal> animalsAfterSim= sim.getAnimals();

        //then
        assertTrue(animalsAfterSim.getFirst().isAt(new Vector2d(4,4)));
        assertTrue(animalsAfterSim.getLast().isAt(new Vector2d(0,0)));
    }

    @Test
    public void animalsDoNotOverlap(){
        //given
        List<Vector2d> startPositions= List.of(new Vector2d(4,4),new Vector2d(4,4));
        List<MoveDirection> moves = List.of(MoveDirection.FORWARD,MoveDirection.BACKWARD);
        Simulation sim = new Simulation(startPositions, moves,new RectangularMap(5,5));

        //when
        sim.run();
        List<Animal> animalsAfterSim= sim.getAnimals();

        //then
        assertTrue(animalsAfterSim.getFirst().isAt(new Vector2d(4,3)));
        assertEquals(1,animalsAfterSim.size());
    }


    @Test
    public void moveParametersParsedCorrectly() throws IllegalArgumentException {
        //given
        List<Vector2d> startPositions= List.of(new Vector2d(2,1),new Vector2d(1,2));
        String[] parameters = {"f", "l","f", "b", "r", "b"};

        //when
        Simulation sim = new Simulation(startPositions,  parse(parameters),new RectangularMap(5,5));
        sim.run();
        List<Animal> animalsAfterSim= sim.getAnimals();

        //then
        assertTrue(animalsAfterSim.getFirst().isAt(new Vector2d(2,3)));
        assertEquals(MapDirection.EAST,animalsAfterSim.getFirst().getCurrentOrientation());

        assertTrue(animalsAfterSim.getLast().isAt(new Vector2d(3,2)));
        assertEquals(MapDirection.WEST,animalsAfterSim.getLast().getCurrentOrientation());
    }


    @Test
    public void throwsOnInvalidArguments(){
        //when
        List<Vector2d> startPositions= List.of(new Vector2d(2,1),new Vector2d(1,2));
        String[] parameters1 = {"F", "l", "f", "b", "r", "b"};
        String[] parameters2 = {"f", "lb","f", "b", "r", "b"};
        String[] parameters3 = {"F", "l","f", "b", "r", "^"};
        String[] parameters4 = {"f", "l","f", "b", "r", "b"};

        //then
        assertThrows(IllegalArgumentException.class, () -> {Simulation sim = new Simulation(startPositions,  parse(parameters1),new RectangularMap(5,5));});
        assertThrows(IllegalArgumentException.class, () -> {Simulation sim = new Simulation(startPositions,  parse(parameters2),new RectangularMap(5,5));});
        assertThrows(IllegalArgumentException.class, () -> {Simulation sim = new Simulation(startPositions,  parse(parameters3),new RectangularMap(5,5));});
        assertDoesNotThrow(() -> {Simulation sim = new Simulation(startPositions,  parse(parameters4),new RectangularMap(5,5));});
    }

    @Test
    public void moveParametersEmpty(){
        //given
        List<Vector2d> startPositions= List.of(new Vector2d(2,1),new Vector2d(1,2));
        String[] parameters = {};

        //when
        Simulation sim = new Simulation(startPositions,  parse(parameters),new RectangularMap(5,5));
        sim.run();
        List<Animal> animalsAfterSim= sim.getAnimals();

        //then
        assertTrue(animalsAfterSim.getFirst().isAt(new Vector2d(2,1)));
        assertEquals(MapDirection.NORTH,animalsAfterSim.getFirst().getCurrentOrientation());

        assertTrue(animalsAfterSim.getLast().isAt(new Vector2d(1,2)));
        assertEquals(MapDirection.NORTH,animalsAfterSim.getLast().getCurrentOrientation());
    }
}
