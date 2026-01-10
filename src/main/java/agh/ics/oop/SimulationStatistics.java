package agh.ics.oop;

import agh.ics.oop.model.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimulationStatistics {

    private final Simulation simulation;

    //data stored in lists for data history and chart display
    private final List<Double> animalsCnt = new ArrayList<>();
    private final List<Double> grassesCnt = new ArrayList<>();
    private final List<Double> freeSpaces = new ArrayList<>();
    private final List<Double> avgEnergy = new ArrayList<>();
    private final List<Double> avgChildrenCnt = new ArrayList<>();
    private final List<Double> avgLifespan = new ArrayList<>();

    public SimulationStatistics(Simulation simulation) {
        this.simulation = simulation;
    }

    private void addAnimalsCnt(){
        animalsCnt.add( (double)simulation.getAnimals().size());
    }

    private void addGrassesCnt(){
        grassesCnt.add( (double) simulation.getMap().getGrasses().size());
    }

    private void addFreeSpaces(){
        //TODO should work after changing getElements to display only first animal on given position
        int width = simulation.getMap().getCurrentBounds().upperRight().x()-simulation.getMap().getCurrentBounds().lowerLeft().x();
        int height = simulation.getMap().getCurrentBounds().upperRight().y()-simulation.getMap().getCurrentBounds().lowerLeft().y();
        freeSpaces.add((double) (width*height-simulation.getMap().getElements().size()));
    }

    public List<List<Integer>> getMostCommonGenomes(int limit) {
        return simulation.getAnimals().stream()
                .collect(Collectors.groupingBy(Animal::getGenes, Collectors.counting()
                ))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }

    private void addAvgEnergy(){
        avgEnergy.add( simulation.getAnimals().stream()
                .mapToInt(Animal::getEnergy)
                .average()
                .orElse(0.0));
    }

    private void addAvgChildrenCnt(){
        avgChildrenCnt.add( simulation.getAnimals().stream()
                .mapToInt(Animal::getChildrenCnt)
                .average()
                .orElse(0.0));
    }

    private void addAvgLifespan(){
        avgLifespan.add( simulation.getDeadAnimals().stream()
                .mapToInt(Animal::getAge)
                .average()
                .orElse(0.0));
    }

    public void update(){
        addAnimalsCnt();
        addGrassesCnt();
        addFreeSpaces();
        addAvgEnergy();
        addAvgLifespan();
        addAvgChildrenCnt();
    }

    public List<Double> getAnimalsCnt() {
        return animalsCnt;
    }

    public List<Double> getGrassesCnt() {
        return grassesCnt;
    }

    public List<Double> getFreeSpaces() {
        return freeSpaces;
    }

    public List<Double> getAvgEnergy() {
        return avgEnergy;
    }

    public List<Double> getAvgChildrenCnt() {
        return avgChildrenCnt;
    }

    public List<Double> getAvgLifespan() {
        return avgLifespan;
    }
}
