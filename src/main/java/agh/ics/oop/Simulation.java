package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{

    private final List<MoveDirection> animalMoves;

    private final List<Animal> animals = new ArrayList<>(); //arraylist for efficient access in iteration

    private final WorldMap map;

    public Simulation(List<Vector2d> startPositions, List<MoveDirection> animalMoves, WorldMap map){
        this.animalMoves = animalMoves;
        this.map = map;
        for (Vector2d position: startPositions){
            Animal animal = new Animal(position);
            try {
                map.place(animal);
                animals.add(animal); //adds animal to list if it can be placed correctly
            }catch (IncorrectPositionException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run(){ //simulates animal moves with given directions
        int currAnimal = 0;
        for (MoveDirection  currMove: animalMoves) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //execute every move on animals in order
            map.move(animals.get(currAnimal),currMove);
            currAnimal=(currAnimal+1)%animals.size();
            //always returns next animal in scope
        }
    }

    public List<Animal> getAnimals() {
        return animals;
    }
}
