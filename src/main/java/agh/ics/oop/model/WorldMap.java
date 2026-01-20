package agh.ics.oop.model;

import agh.ics.oop.model.enums.SimulationParameters;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.GenomeGenerator;
import agh.ics.oop.model.util.RandomGrassGenerator;
import javafx.util.Pair;

import java.util.*;

public class WorldMap implements MoveValidator {
    private final Boundary mapBounds;
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Boundary jungleBounds;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final ArrayList<MapChangeListener> listeners = new ArrayList<>();
    private final UUID mapId = UUID.randomUUID();
    private final SimulationParameters parameters;

    public WorldMap(SimulationParameters parameters) {
        this.parameters = parameters;

        mapBounds = new Boundary(new Vector2d(0, 0), new Vector2d(parameters.mapWidth() - 1, parameters.mapHeight() - 1));
        jungleBounds = calculateJungle(parameters.mapWidth(), parameters.mapHeight());

        growGrass(parameters.initialGrassAmount());
    }

    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }

    public List<Vector2d> getAnimalFields() {
        return animals.keySet().stream().filter(field -> !animals.get(field).isEmpty()).toList();
    }

    public UUID getId(){
        return mapId;
    }

    public Boundary getJungleBounds() {
        return jungleBounds;
    }

    // TODO remove or use in testing
    public List<WorldElement> getElements(){
        List<WorldElement> grassAndAnimals = new ArrayList<>(grasses.values());

        grassAndAnimals.addAll(animals.values().stream().map((List<Animal> animalsOnField) -> animalsOnField.isEmpty() ? null : animalsOnField.getFirst()).filter(Objects::nonNull).toList());

        return grassAndAnimals;
    }

    public Boundary getCurrentBounds() {
        return mapBounds;
    }

    public WorldElement objectAt(Vector2d position){
        if (animals.containsKey(position) && !animals.get(position).isEmpty()) {
            return animals.get(position).getFirst();
        }

        return grasses.get(position);
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
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
            return new Pair<>(facing, position);
        }

        return new Pair<>(facing, newPosition);
    }

    public void subscribe(MapChangeListener listener){
        listeners.add(listener);
    }

    public void mapChanged(){
        for (MapChangeListener listener : listeners){
            listener.mapChanged(this);
        }
    }

    public void place(Animal animal) throws IncorrectPositionException {
        if (!animals.containsKey(animal.getPosition())) {
            animals.put(animal.getPosition(), new ArrayList<>());
        }

        animals.get(animal.getPosition()).add(animal);

        mapChanged();
    }

    public void move(Animal animal) {
        Vector2d posBeforeMove = animal.getPosition();
        List<Animal> fieldBeforeMove = animals.get(posBeforeMove);

        if (fieldBeforeMove.contains(animal)) {
            fieldBeforeMove.remove(animal);

            animal.move(this);
            animal.useEnergy(parameters.dailyEnergyLoss());

            if (!animals.containsKey(animal.getPosition())) {
                animals.put(animal.getPosition(), new ArrayList<>());
            }

            animals.get(animal.getPosition()).add(animal);
        }
    }

    /**
     * @return whether the animal crashed into another
     */
    public boolean fastMove(Animal animal, int step) {
        Vector2d posBeforeMove = animal.getPosition();
        List<Animal> fieldBeforeMove = animals.get(posBeforeMove);

        if (fieldBeforeMove.contains(animal)) {
            fieldBeforeMove.remove(animal);

            if (step == 1) {
                animal.move(this);
            } else {
                animal.moveExtra(this);
            }

            Vector2d field = animal.getPosition();

            grasses.remove(field);

            if (!animals.containsKey(field)) {
                animals.put(field, new ArrayList<>());
            }

            animals.get(field).add(animal);

            if (animals.get(field).size() > 1) {
                animal.useEnergy(parameters.dailyEnergyLoss() * 2);

                return true;
            }

            if (step == animal.getSpeed()) animal.useEnergy(parameters.dailyEnergyLoss());
        }

        return false;
    }

    public List<Animal> removeDeadAnimals() {
        List<Animal> deadAnimals = new ArrayList<>();

        for (Vector2d field : animals.keySet()) {
            List<Animal> animalsOnField = animals.get(field);
            List<Animal> deadAnimalsOnField = new ArrayList<>();

            for (Animal animal : animalsOnField) {
                if (animal.getEnergy() <= 0) {
                    deadAnimalsOnField.add(animal);
                }
            }

            animalsOnField.removeAll(deadAnimalsOnField);
            deadAnimals.addAll(deadAnimalsOnField);
        }

        return deadAnimals;
    }

    public void consumeGrass() {
        List<Vector2d> consumedFields = new ArrayList<>();

        for (Vector2d field : grasses.keySet()) {
            if (!animals.containsKey(field) || animals.get(field).isEmpty()) continue;

            Animal chosenAnimal = animals.get(field).stream().sorted().toList().getFirst();

            chosenAnimal.eat(grasses.get(field), parameters.grassEnergy());
            consumedFields.add(field);
        }

        for (Vector2d field : consumedFields) {
            grasses.remove(field);
        }
    }

    public List<Animal> copulate() {
        GenomeGenerator genomeGenerator = new GenomeGenerator(parameters.genomeLength());
        List<Animal> newbornAnimals = new ArrayList<>();

        for (Vector2d field : animals.keySet()) {
            List<Animal> animalsOnField = animals.get(field);

            if (animalsOnField.isEmpty()) continue;

            List<Animal> animalsToCopulate = animalsOnField.stream().filter(animal -> animal.getEnergy() > parameters.reproductionReadyEnergy()).sorted().limit(2).toList();

            if (animalsToCopulate.size() < 2) continue;

            Animal strongerParent = animalsToCopulate.get(0);
            Animal weakerParent = animalsToCopulate.get(1);

            List<Integer> newGenome = genomeGenerator.generateGenome(strongerParent, weakerParent);

            Animal newborn = new Animal(field, newGenome, parameters.copulationEnergyLoss() * 2);

            animalsOnField.add(newborn);
            newbornAnimals.add(newborn);

            strongerParent.hasReproduced(parameters.copulationEnergyLoss());
            weakerParent.hasReproduced(parameters.copulationEnergyLoss());
        }

        return newbornAnimals;
    }

    private Boundary calculateJungle(int width, int height) {

        int jungleHeight = Math.max(1, (int) Math.round( height * 0.2));
        int yStart = (height - jungleHeight) / 2;

        return new Boundary(
                new Vector2d(0, yStart),
                new Vector2d(width-1, yStart + jungleHeight - 1)
        );
    }

    public void growGrass(int grassCnt){
        RandomGrassGenerator generator = new RandomGrassGenerator(mapBounds,jungleBounds,grassCnt,new HashSet<>(grasses.keySet()));
        for (Vector2d newPos : generator){
            grasses.put(newPos,new Grass(newPos));
        }
    }
}
