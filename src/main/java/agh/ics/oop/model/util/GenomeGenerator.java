package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Genome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenomeGenerator {
    private final int genomeLength;
    private final int maxMutationCnt;
    private final int minMutationCnt;

    public GenomeGenerator(int genomeLength, int maxMutationCnt, int minMutationCnt) {
        this.genomeLength = genomeLength;
        this.maxMutationCnt = maxMutationCnt;
        this.minMutationCnt = minMutationCnt;
    }

    /**
     * generates new genome as a slice of stronger parent's genome concatenated with weake ones
     * @param strongerParent stronger parent that copulates
     * @param weakerParent weaker parent that copulates
     * @return genome of the child
     */
    public Genome generateGenome(Animal strongerParent, Animal weakerParent) {
        int strongerParentEnergy = strongerParent.getEnergy();
        int weakerParentEnergy = weakerParent.getEnergy();

        List<Integer> strongerParentGenes = strongerParent.getGenome().getGenes();
        List<Integer> weakerParentGenes = weakerParent.getGenome().getGenes();

        int genomeSplitPoint = getGenomeSplitPoint(strongerParentEnergy, weakerParentEnergy);

        List<Integer> newGeneList = new ArrayList<>();

        for (int i = 0; i < genomeSplitPoint; i++) {
            newGeneList.add(strongerParentGenes.get(i));
        }

        for (int i = genomeSplitPoint; i < genomeLength; i++) {
            newGeneList.add(weakerParentGenes.get(i));
        }

        Genome newGenome = new Genome(newGeneList);
        mutate(newGenome);

        return new Genome(newGeneList);
    }

    /**
     * calculates the point which separates parents genome slices
     * @param strongerParentEnergy energy od the stronger parent before copulation
     * @param weakerParentEnergy energy of the weaker parent before copulation
     * @return index in genome array to split
     */
    private int getGenomeSplitPoint(int strongerParentEnergy, int weakerParentEnergy) {
        float genomeSplitRatio = (float) strongerParentEnergy / (strongerParentEnergy + weakerParentEnergy);
        int genomeSplitPoint = (int) (genomeLength * genomeSplitRatio);

        return Math.random() < 0.5 ? genomeSplitPoint : genomeLength - genomeSplitPoint;
    }

    /**
     * mutates random amount of random genes in a genome
     * allows mutations to happen multiple times on the same gene during one mutation
     * @param genome genome to mutate
     */
    private void mutate(Genome genome) {
        int mutatedCount = minMutationCnt + (int) (Math.random() * maxMutationCnt);

        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < genomeLength; i++) {
            positions.add(i);
        }

        Collections.shuffle(positions);

        for (int i = 0; i < mutatedCount; i++) {
            genome.setRandomGene(positions.get(i%positions.size()));
        }
    }
}
