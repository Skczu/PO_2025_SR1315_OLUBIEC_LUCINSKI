package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.enums.SimulationParameters;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation implements Runnable {
    private final List<Animal> animals = new ArrayList<>();
    private final List<Animal> deadAnimals = new ArrayList<>(); //for statistics purposes
    private final WorldMap map;
    private boolean paused = false;

    private final SimulationStatistics simulationStatistics;
    private final StatisticsExporter exporter;
    private final SimulationParameters parameters;

    public Simulation(WorldMap map, SimulationParameters parameters){
        this.map = map;
        this.parameters = parameters;

        Vector2d topRightCorner = map.getCurrentBounds().upperRight();
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(topRightCorner.x(), topRightCorner.y(), parameters.initialAnimalAmount());

        List<Integer> generatedGenes = new ArrayList<>();
        for (int i = 0; i < parameters.genomeLength(); i++) {
            generatedGenes.add(i);
        }

        for (Vector2d position : randomPositionGenerator) {
            Collections.shuffle(generatedGenes);

            Animal animal = new Animal(position, List.copyOf(generatedGenes), parameters.initialAnimalEnergy());

            map.place(animal);
            animals.add(animal);
        }

        simulationStatistics = new SimulationStatistics(this);
        simulationStatistics.update();

        exporter = new StatisticsExporter(simulationStatistics, this.map.getId());
    }

    @Override
    public void run() {
        while (true) {
            waitDay();

            handlePause();

            removeDeadAnimals();

            if (parameters.isFastAnimals()) {
                simulateFastAnimalsMovement();
            } else {
                simulateMovement();
            }

            consumeGrass();

            map.growGrass(parameters.dailyGrassGrowth());

            copulate();

            map.mapChanged();

            simulationStatistics.update();
            try {
                exporter.export();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean togglePause() {
        paused = !paused;

        if (!paused) notifyAll();

        return paused;
    }

    private synchronized void handlePause() {
        while (paused) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void waitDay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void removeDeadAnimals() {
        List<Animal> diedToday = map.removeDeadAnimals();

        deadAnimals.addAll(diedToday);
        animals.removeAll(diedToday);
    }

    private void simulateMovement() {
        for (Animal animal : animals) {
            map.move(animal);
        }
    }

    private void simulateFastAnimalsMovement() {
        if (animals.isEmpty()) return;

        for (Animal animal : animals) {
            animal.setSpeed(parameters.fastAnimalsEnergyThreshold(), parameters.fastAnimalsSpeedIncreaseThreshold(), parameters.fastAnimalsMaxSpeed());

            int speed = animal.getSpeed();

            if (speed > 1) {
                for (int i = 1; i <= speed; i++) {
                    if (map.fastMove(animal, i)) break;
                }
            } else {
                map.move(animal);
            }
        }
    }

    private void consumeGrass() {
        map.consumeGrass();
    }

    private void copulate() {
        animals.addAll(map.copulate());
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Animal> getDeadAnimals() {
        return deadAnimals;
    }

    public WorldMap getMap() {
        return map;
    }

    public SimulationParameters getParameters() {
        return parameters;
    }

    public SimulationStatistics getSimulationStatistics() {
        return simulationStatistics;
    }
}
