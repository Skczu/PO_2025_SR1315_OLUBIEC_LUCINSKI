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

    private int getGenomeSplitPoint(int strongerParentEnergy, int weakerParentEnergy) {
        float genomeSplitRatio = (float) strongerParentEnergy / (strongerParentEnergy + weakerParentEnergy);
        int genomeSplitPoint = (int) (genomeLength * genomeSplitRatio);

        return Math.random() < 0.5 ? genomeSplitPoint : genomeLength - genomeSplitPoint;
    }

    //mutates genes by
    private void mutate(Genome genome) {
        int mutatedCount = minMutationCnt + (int) (Math.random() * Math.min(maxMutationCnt,genomeLength));

        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < genomeLength; i++) {
            positions.add(i);
        }

        Collections.shuffle(positions);
        positions = positions.subList(0,mutatedCount+1);

        for (int i = 0; i < mutatedCount; i++) {
            genome.setRandomGene(positions.get(i));
        }
    }
}
