package agh.ics.oop.model.enums;

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
        int genomeLength
) {}
