package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {

    protected final Map<Vector2d, Animal> animals = new HashMap<>();

    private final ArrayList<MapChangeListener> listeners = new ArrayList<>();

    public Map<Vector2d, Animal> getAnimals() { //for testing purposes
        return animals;
    }

    protected final MapVisualizer visualiser = new MapVisualizer(this);

    private final UUID mapId=UUID.randomUUID();

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }


    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())){ //places animal only on valid unoccupied positions
            animals.put(animal.getPosition(),animal);
            mapChanged("New animal was placed at: " + animal.getPosition()); //notifies for placing
        }else{
            throw new IncorrectPositionException(animal.getPosition());
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position){
        return animals.get(position);
    }


    @Override
    public void move(Animal animal, MoveDirection direction) {
        Vector2d posBeforeMove = animal.getPosition();
        if (animals.get(posBeforeMove)==animal) { //checks if passed animal is on our map
            animals.remove(posBeforeMove);
            animal.move(direction, this);
            animals.put(animal.getPosition(), animal);
            mapChanged("Animal has moved from: " + posBeforeMove + " to " + animal.getPosition()); //notifies for movement
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        //unlimited map so move possible to every field unoccupied by animal
        return (!animals.containsKey(position));
    }


    @Override
    public List<WorldElement> getElements(){
        //returns all animals
        return new ArrayList<>(animals.values());
    }


    @Override
    public String toString() {
        //finds furthest to Upper right, and to lower left currently occupied position by anything
        Boundary bounds = getCurrentBounds();
        return visualiser.draw(bounds.lowerLeft(),bounds.upperRight());
    }

    @Override
    public UUID getId(){
        return mapId;
    }

    @Override
    public void subscribe(MapChangeListener listener){
        listeners.add(listener);
    }

    @Override
    public void unSubscribe(MapChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void mapChanged(String message){
        for(MapChangeListener listener : listeners){
            listener.mapChanged(this,message);
        }
    }
}
