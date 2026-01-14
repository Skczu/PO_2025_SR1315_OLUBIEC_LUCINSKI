package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.RandomPositionGenerator;
import javafx.util.Pair;

import java.util.*;

public class WorldMap implements MoveValidator {
    private final Boundary mapBounds;
    private final Map<Vector2d, Animal> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final ArrayList<MapChangeListener> listeners = new ArrayList<>();
    private final UUID mapId = UUID.randomUUID();

    public WorldMap(int mapWidth, int mapHeight, int grassCount) {
        mapBounds = new Boundary(new Vector2d(0, 0), new Vector2d(mapWidth - 1, mapHeight - 1));

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(mapWidth - 1, mapHeight - 1, grassCount);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    public Map<Vector2d, Animal> getAnimals() { //for testing purposes
        return animals;
    }

    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }

    public UUID getId(){
        return mapId;
    }

    public List<WorldElement> getElements(){
        List<WorldElement> grassAndAnimals = new ArrayList<>(grasses.values());

        grassAndAnimals.addAll(animals.values());

        return grassAndAnimals;
    }

    public Boundary getCurrentBounds() {
        return mapBounds;
    }

    public WorldElement objectAt(Vector2d position){
        if (animals.get(position) != null) {
            return animals.get(position);
        }

        return grasses.get(position);
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public boolean canMoveTo(Vector2d position) {
        return !animals.containsKey(position);
    }

    @Override
    public Pair<MapDirection, Vector2d> positionAfterMove(MapDirection facing, Vector2d position) {
        Vector2d newPosition = position.add(facing.toUnitVector());

        if (newPosition.y()>mapBounds.upperRight().y() || newPosition.y()<mapBounds.lowerLeft().y()){
            //bounces of the board on north and south ensures valid y
            facing=facing.bounce();
            newPosition = position.add(facing.toUnitVector());
        }

        //x coordinate is always moduled by map width so the animal goes out on the other end ensures valid x
        //uses Math.FloorMod instead of % to work properly with x<0
        newPosition = new Vector2d(Math.floorMod(newPosition.x(),mapBounds.upperRight().x()+1), newPosition.y());

        //validated new position for crazy cases like 1x1 map
        if(!newPosition.precedes(mapBounds.upperRight()) || !newPosition.follows(mapBounds.lowerLeft())){
            return new Pair<>(facing, newPosition);
        }

        //for now checks if occupied by another animal
        //TODO delete after enabling multiple animals on one field
        if (!canMoveTo(newPosition)){
            return new Pair<>(facing, position);
        }

        return new Pair<>(facing, newPosition);
    }

    public void subscribe(MapChangeListener listener){
        listeners.add(listener);
    }

    public void unsubscribe(MapChangeListener listener) {
        listeners.remove(listener);
    }

    public void mapChanged(String message){
        for (MapChangeListener listener : listeners){
            listener.mapChanged(this, message);
        }
    }

    public void place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())){ //places animal only on valid unoccupied positions
            animals.put(animal.getPosition(),animal);
            mapChanged("New animal was placed at: " + animal.getPosition()); //notifies for placing
        } else{
            throw new IncorrectPositionException(animal.getPosition());
        }
    }

    public void move(Animal animal) {
        Vector2d posBeforeMove = animal.getPosition();

        if (animals.get(posBeforeMove) == animal) { //checks if passed animal is on our map
            animals.remove(posBeforeMove);
            animal.move(this);
            animals.put(animal.getPosition(), animal);
        }
    }
}
