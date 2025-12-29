package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.*;

public class RectangularMap implements WorldMap {

    //Set or hashmap of lists?
    //keep sorted order of animals on given position?
    //One list for all or separate for grass??
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    private final ArrayList<MapChangeListener> listeners = new ArrayList<>();

    public Map<Vector2d, List<Animal>> getAnimals() {
        return animals;
    }

    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }

    protected final MapVisualizer visualiser = new MapVisualizer(this);

    private final UUID mapId=UUID.randomUUID();

    //width and height of map rectangle represented by lower left and upper right corners coordinates
    private final Vector2d minimalPos;
    private final Vector2d maximalPos;


    public RectangularMap(int width, int height) {
        minimalPos = new Vector2d(0,0);
        maximalPos = new Vector2d(width-1,height-1);
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(maximalPos.getX()+1, maximalPos.getY()+1, 1000);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }


    @Override
    public void place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())){ //places animal only on valid unoccupied positions
            animals.add(animal);
            mapChanged("New animal was placed at: " + animal.getPosition()); //notifies for placing
        }else{
            throw new IncorrectPositionException(animal.getPosition());
        }
    }

    //compare all objects with given position
    @Override
    public WorldElement objectAt(Vector2d position){
        return null;
    }

    //
    public void growGrasses(){

    }

    //only first two animals on a given position do reproduce
    public void reproduce(Animal firstParent, Animal secondParent){
        firstParent.copulate(3);
        secondParent.copulate(3);

        animals.put(firstParent.getPosition(),new Animal(firstParent.getPosition(),List.of(1,2,3),6));
    }

    public void removeDead(){
        for(WorldElement element : animals){
            if (element.getClass()== Animal.class && ((Animal) element).getEnergy()<=0){
                animals.remove(element);
            }
        }
    }


    //to fix
    @Override
    public void move(Animal animal) {
        Vector2d posBeforeMove = animal.getPosition();
        if (animals.contains(animal)) { //checks if passed animal is on our map
            animals.remove(animal);
            animal.move(this);



            animals.add(animal);



            mapChanged("Animal has moved from: " + posBeforeMove + " to " + animal.getPosition()); //notifies for movement
        }
    }



    @Override
    public boolean canMoveTo(Vector2d position) {
        //unlimited map so move possible to every field unoccupied by animal
        return true;
    }


    @Override
    public String toString() {
        //finds furthest to Upper right, and to lower left currently occupied position by anything
        Boundary bounds = getCurrentBounds();
        return visualiser.draw(bounds.lowerLeft(),bounds.upperRight());
    }


    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(minimalPos,maximalPos);
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
