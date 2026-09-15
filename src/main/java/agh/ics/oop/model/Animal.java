package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;
import javafx.util.Pair;

import java.util.Comparator;
import java.util.List;

public class Animal implements WorldElement, Comparable<Animal> {
    private MapDirection currentOrientation;

    private Vector2d mapPosition;

    private final Genome genes;

    private int usingGene;

    private int energy;

    private int age;

    private int childrenCnt;

    private int speed;

    public Animal(Vector2d mapPosition, Genome genes, int startEnergy){
        this.currentOrientation = MapDirection.NORTH;
        this.mapPosition = mapPosition;
        this.usingGene = 0;
        this.age = 0;
        this.childrenCnt = 0;
        this.genes = genes;
        this.energy = startEnergy;
        this.speed = 1;
    }

    @Override
    public String toString() {
        return currentOrientation.toString();
    }

    @Override
    public boolean isAt(Vector2d position){
        return mapPosition.equals(position);
    }

    public void move(MoveValidator moveValidator){
        currentOrientation = currentOrientation.rotate(genes.getGenes().get(usingGene));

        Pair<MapDirection,Vector2d> newPosition = moveValidator.positionAfterMove(currentOrientation,mapPosition);

        currentOrientation = newPosition.getKey();
        mapPosition = newPosition.getValue();

        usingGene = (usingGene + 1) % genes.getGenes().size();
    }

    public void moveExtra(MoveValidator moveValidator) {
        Pair<MapDirection,Vector2d> newPosition = moveValidator.positionAfterMove(currentOrientation,mapPosition);

        currentOrientation = newPosition.getKey();
        mapPosition = newPosition.getValue();
    }

    public void hasReproduced(int consumedEnergy){
        energy -= consumedEnergy;
        childrenCnt += 1;
    }

    public void eat(Grass grass,int grassEnergy){
        if (grass.getPosition().equals(mapPosition)){
            energy += grassEnergy;
        }
    }

    public void useEnergy(int dailyConsumption){
        energy -= dailyConsumption;
        age += 1;
    }

    public void setSpeed(int fastAnimalsEnergyThreshold, int fastAnimalsSpeedIncreaseThreshold, int fastAnimalsMaxSpeed){
        if (energy >= fastAnimalsMaxSpeed) return;

        if (energy > fastAnimalsEnergyThreshold) {
            speed = 1 + (energy - fastAnimalsEnergyThreshold + 1) / fastAnimalsSpeedIncreaseThreshold;
        }
    }

    @Override
    public int compareTo(Animal other) {
        return Comparator
                .comparingInt(Animal::getEnergy).reversed()
                .thenComparingInt(a -> a.age).reversed()
                .thenComparingInt(a -> a.childrenCnt).reversed()
                .compare(this, other);
    }

    @Override
    public Vector2d getPosition() {
        return mapPosition;
    }

    public MapDirection getCurrentOrientation() {
        return currentOrientation;
    }

    public Genome getGenes() {
        return genes;
    }

    public int getChildrenCnt() {
        return childrenCnt;
    }

    public int getAge() {
        return age;
    }

    public int getEnergy(){
        return energy;
    }

    public int getSpeed() {
        return speed;
    }
}
