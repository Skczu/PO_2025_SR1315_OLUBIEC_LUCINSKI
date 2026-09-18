package agh.ics.oop.model.enums;

/**
 * class for storing and providing simulation parameters set in Presenter to Simulation class
 */


public record SimulationParameters(
        int mapWidth,
        int mapHeight,
        boolean isFastAnimals,
        int initialAnimalAmount,
        int initialAnimalEnergy,
        int dailyEnergyLoss,
        int reproductionReadyEnergy,
        int copulationEnergyLoss,
        int initialGrassAmount,
        int dailyGrassGrowth,
        int grassEnergy,
        int minMutationAmount,
        int maxMutationAmount,
        int genomeLength,
        int fastAnimalsEnergyThreshold,
        int fastAnimalsSpeedIncreaseThreshold,
        int fastAnimalsMaxSpeed
) {}
