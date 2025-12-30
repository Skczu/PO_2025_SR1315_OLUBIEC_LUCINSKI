package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;
import javafx.util.Pair;

import java.util.*;

public class GrassField extends AbstractWorldMap{

    //implementing on separate HashMaps as suggested in exercise description
    //solution on one hashmap would not be able to store grass and animal on the same position
    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    public GrassField(int grassCount){
        int max = (int) Math.sqrt(grassCount * 10);
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(max+1, max+1, grassCount);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (super.objectAt(position) == null){
            return grasses.get(position);
        }
        return super.objectAt(position); //the animal has priority over grass
    }

    @Override
    public List<WorldElement> getElements(){
        //returns all animals and grasses
        // an animal can stand on a grass so hashmap with Vector2d key of both impossible to do
        List<WorldElement> grassAndAnimals = new ArrayList<>(grasses.values());
        grassAndAnimals.addAll(super.getElements());
        return grassAndAnimals;
    }

    @Override
    public Boundary getCurrentBounds() {
        List<WorldElement> elements = getElements();
        Vector2d upperRight = elements.getFirst().getPosition();
        Vector2d lowerLeft = elements.getFirst().getPosition();

        for(WorldElement elem : elements){
            upperRight = upperRight.upperRight(elem.getPosition());
            lowerLeft = lowerLeft.lowerLeft(elem.getPosition());
        }
        return new Boundary(lowerLeft,upperRight);
    }

    //so far allows infinite movement
    @Override
    public Pair<MapDirection, Vector2d> positionAfterMove(MapDirection facing, Vector2d position) {
        return new Pair<MapDirection,Vector2d>(facing,position.add(facing.toUnitVector()));
    }
}
