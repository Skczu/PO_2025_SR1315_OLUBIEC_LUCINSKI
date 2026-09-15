package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;

import java.util.ArrayList;
import java.util.List;


public class Genome {

    static final int GENES_VARIETY = 8; //genes can be from 0 to 7

    //singular genes of each animal
    private final List<Integer> genes;

    //gene currently in use by animal
    private int currentlyUsed;

    //genome from random genes
    public Genome(int length){
        this.genes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            genes.add((int) (Math.random() * GENES_VARIETY));
        }
        this.currentlyUsed=0;
    }

    //genome from given genes
    public Genome(List<Integer> genes){
        this.genes = genes;
        this.currentlyUsed=0;
    }

    //TODO implement next gene and other methods
    public List<Integer> getGenes() {
        return genes;
    }


    @Override
    public String toString() {
        String genString = "[";
        for (Integer val : genes){
            genString += val.toString();
            genString+=",";
        }
        genString = genString.substring(1,genString.length()-1);
        genString+="]";
        return genString;
    }

    public int getCurrentlyUsed() {
        return currentlyUsed;
    }

    public void setCurrentlyUsed(int currentlyUsed) {
        this.currentlyUsed = currentlyUsed;
    }

    //returns information about which direction to turn and switches to next gene
    public MapDirection getNextDirection(){
        currentlyUsed = (currentlyUsed + 1) % genes.size();
        return MapDirection.fromDigit(currentlyUsed);
    }
}
