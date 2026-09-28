package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;

import java.util.ArrayList;
import java.util.List;



public class Genome {

    /**
     * genes can be from 0 to 7
     */
    static final int GENES_VARIETY = 8;

    /**
     * singular genes of each animal
     */
    private final List<Integer> genes;

    /**
     * gene currently in use by animal
     */
    private int currentlyUsed;

    /**
     * genome of random genes
     * @param length length of new genome
     */
    public Genome(int length){
        this.genes = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            genes.add((int) (Math.random() * GENES_VARIETY));
        }
        this.currentlyUsed=0;
    }

    /**
     * genome from given genes
     * @param genes list of genome integers
     */
    public Genome(List<Integer> genes){
        if (genes.isEmpty()){
            throw new IllegalArgumentException("genome cannot be empty");
        }
        this.genes = genes;
        this.currentlyUsed=0;
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public void setRandomGene(int genomePosition){
        if (genomePosition<0 || genomePosition>=genes.size()){
            throw new IllegalArgumentException("Invalid position in genome: "+genomePosition);
        }
        genes.set(genomePosition,(int) (Math.random() * GENES_VARIETY));
    }

    @Override
    public String toString() {
        return genes.toString().replace(" ", "");
    }

    public int getCurrentlyUsed() {
        return currentlyUsed;
    }

    public void setCurrentlyUsed(int currentlyUsed) {
        this.currentlyUsed = currentlyUsed;
    }

    /**
     * @return information about which direction to turn and switches to next gene
     */
    public MapDirection getNextDirection(){
        int toReturn = currentlyUsed;
        currentlyUsed = (currentlyUsed + 1) % genes.size();
        return MapDirection.fromDigit(genes.get(toReturn));
    }
}
