package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenomeGenerator {
    private final int genomeLength;

    public GenomeGenerator(int genomeLength) {
        this.genomeLength = genomeLength;
    }

    public List<Integer> generateGenome(Animal strongerParent, Animal weakerParent) {
        int strongerParentEnergy = strongerParent.getEnergy();
        int weakerParentEnergy = weakerParent.getEnergy();
        //TODO implement genome better
        List<Integer> strongerParentGenes = strongerParent.getGenes().getGenes();
        List<Integer> weakerParentGenes = weakerParent.getGenes().getGenes();

        int genomeSplitPoint = getGenomeSplitPoint(strongerParentEnergy, weakerParentEnergy);

        List<Integer> newGenome = new ArrayList<>();

        for (int i = 0; i < genomeSplitPoint; i++) {
            newGenome.add(strongerParentGenes.get(i));
        }

        for (int i = genomeSplitPoint; i < genomeLength; i++) {
            newGenome.add(weakerParentGenes.get(i));
        }

        mutate(newGenome);

        return newGenome;
    }

    private int getGenomeSplitPoint(int strongerParentEnergy, int weakerParentEnergy) {
        float genomeSplitRatio = (float) strongerParentEnergy / (strongerParentEnergy + weakerParentEnergy);
        int genomeSplitPoint = (int) (genomeLength * genomeSplitRatio);

        return Math.random() < 0.5 ? genomeSplitPoint : genomeLength - genomeSplitPoint;
    }

    private void mutate(List<Integer> genome) {
        int mutatedCount = 1 + (int) (Math.random() * genomeLength);

        List<Integer> positions = new ArrayList<>();

        for (int i = 0; i < mutatedCount; i++) {
            positions.add(i);
        }

        Collections.shuffle(positions);

        for (int i = 0; i < mutatedCount; i++) {
            int newSingleMutation = (int) (Math.random() * genomeLength);

            genome.set(positions.get(i), newSingleMutation);
        }
    }
}
