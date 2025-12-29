package agh.ics.oop.model;

import java.util.Comparator;
import java.util.List;

public class Animal implements WorldElement, Comparable<Animal>{

    private MapDirection currentOrientation;

    private Vector2d mapPosition;

    private final List<Integer> genes;

    private int usingGene;

    private int energy;

    private int age;

    private int childrenCnt;

    public Animal(Vector2d mapPosition, List<Integer> genes, int startEnergy){
        this.currentOrientation = MapDirection.NORTH;
        this.mapPosition = mapPosition;
        this.genes = genes;
        this.energy = startEnergy;
        this.usingGene =0;
        this.age=0;
        this.childrenCnt = 0;
    }

    @Override
    public Vector2d getPosition() {
        return mapPosition;
    }

    //getter for checking orientation in tests
    public MapDirection getCurrentOrientation() {
        return currentOrientation;
    }

    public int getEnergy(){
        return energy;
    }

    @Override
    public String toString() { //returns only schematic animal position ex. N for NORTH
        return currentOrientation.toString();
    }

    @Override
    public boolean isAt(Vector2d position){
        return mapPosition.equals(position);
    }

    //to fix
    public void move(MoveValidator moveValidator){
        //potential position after move
//        Vector2d newPosition = mapPosition;
//
//        newPosition = newPosition.add(currentOrientation.toUnitVector());
//
//        //prevent leaving map - box defined atop the class
//        //updates actual position only to valid values
//        if (moveValidator.canMoveTo(newPosition) ){
//            mapPosition = newPosition;
//        }
        //how to handle borders?????
         currentOrientation = currentOrientation.rotate(genes.get(usingGene));
         mapPosition = mapPosition.add(currentOrientation.toUnitVector());
         usingGene = (usingGene+1)%genes.size();
    }

    public void copulate(int consumedEnergy){
        energy-=consumedEnergy;
        childrenCnt+=1;
    }

    public void eat(Grass grass,int grassEnergy){
        if (grass.getPosition()==mapPosition){
            energy+=grassEnergy;
        }
    }

    public void useEnergy(int dailyConsumption){
        energy-=dailyConsumption;
        age+=1;
    }

    @Override
    public int compareTo(Animal other) {
        return Comparator
                .comparingInt(Animal::getEnergy).reversed()
                .thenComparingInt(a -> a.age).reversed()
                .thenComparingInt(a -> a.childrenCnt).reversed()
                .compare(this, other);
    }
}
