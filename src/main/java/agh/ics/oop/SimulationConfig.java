package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.List;

public record SimulationConfig(int mapWidth,
                               int mapHeight,
                               int initialAnimalAmount,
                               int initialAnimalEnergy,
                               int dailyEnergyLoss,
                               int reproductionReadyEnergy,
                               int copulationEnergyLoss,
                               int initialGrassAmount,
                               int dailyGrassGrowth,
                               int grassEnergy,
                               int minimumMutationAmount,
                               int maximumMutationAmount,
                               int genomeLength,

                               //temporary
                               List<MoveDirection> directions) {
}
