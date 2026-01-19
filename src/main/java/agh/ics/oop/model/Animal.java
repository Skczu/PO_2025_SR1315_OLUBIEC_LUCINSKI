package agh.ics.oop.model;

import javafx.util.Pair;

import java.util.Comparator;
import java.util.List;

public class Animal implements WorldElement, Comparable<Animal> {

    static final int MINIMAL_FAST_ENERGY=20;

    private MapDirection currentOrientation;

    private Vector2d mapPosition;

    private final List<Integer> genes;

    private int usingGene;

    private int energy;

    private int age;

    private int childrenCnt;

    private int speed;

    public Animal(Vector2d mapPosition, List<Integer> genes, int startEnergy){
        this.currentOrientation = MapDirection.NORTH;
        this.mapPosition = mapPosition;
        this.usingGene = 0;
        this.age = 0;
        this.childrenCnt = 0;
        this.genes = genes;
        this.energy = startEnergy;
        this.speed=1;
    }

    @Override
    public Vector2d getPosition() {
        return mapPosition;
    }

    //getter for checking orientation in tests
    public MapDirection getCurrentOrientation() {
        return currentOrientation;
    }

    @Override
    public String toString() { //returns only schematic animal position ex. N for NORTH
        return currentOrientation.toString();
    }

    @Override
    public boolean isAt(Vector2d position){
        return mapPosition.equals(position);
    }

    public void move(MoveValidator moveValidator){
        //position after move
        currentOrientation = currentOrientation.rotate(genes.get(usingGene));

        Pair<MapDirection,Vector2d> newPosition = moveValidator.positionAfterMove(currentOrientation,mapPosition);

        currentOrientation = newPosition.getKey();
        mapPosition = newPosition.getValue();

        usingGene = (usingGene + 1) % genes.size();
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

    public void setSpeed(){
        if(energy>MINIMAL_FAST_ENERGY){
            speed=1+MINIMAL_FAST_ENERGY-energy;
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

    public List<Integer> getGenes() {
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
}
