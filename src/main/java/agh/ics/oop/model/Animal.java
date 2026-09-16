package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;
import javafx.util.Pair;

import java.util.Comparator;

public class Animal implements WorldElement, Comparable<Animal> {

    private MapDirection currentOrientation;

    private Vector2d mapPosition;

    private final Genome genome;

    private int energy;

    private int age;

    private int childrenCnt;

    private int speed;

    public Animal(Vector2d mapPosition, Genome genome, int startEnergy){
        this.currentOrientation = MapDirection.NORTH;
        this.mapPosition = mapPosition;
        this.age = 0;
        this.childrenCnt = 0;
        this.genome = genome;
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

    public void useEnergy(int consumption){
        energy -= consumption;
    }

    public void liveOneDay(){
        age+=1;
    }

    public void setSpeed(int fastAnimalsEnergyThreshold, int fastAnimalsSpeedIncreaseThreshold, int fastAnimalsMaxSpeed){
        if (energy >= fastAnimalsMaxSpeed) {
         speed=1;
         return;
        }

        if (energy > fastAnimalsEnergyThreshold) {
            speed = 1 + (energy - fastAnimalsEnergyThreshold + 1) / fastAnimalsSpeedIncreaseThreshold;
        }
    }

    //decides which animal is superior and eats before others on the same field
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

    public Genome getGenome() {
        return genome;
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

    public void rotate(){
        currentOrientation = currentOrientation.rotate(currentOrientation, genome.getNextDirection());
    }

}
