package agh.ics.oop;

import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.UUID;

public class StatisticsExporter {

    private final SimulationStatistics statistics;
    private final UUID mapUid; //for multiple simulations multiple files

    public StatisticsExporter(SimulationStatistics statistics,UUID mapUid) {
        this.statistics = statistics;
        this.mapUid=mapUid;
    }

    public void export() throws IOException {
        try (FileWriter writer = new FileWriter("statistics"+mapUid+".csv", false)) {

            List<List<Integer>> genomes = statistics.getMostCommonGenomes(3);

            writer.append("animalsCnt");
            for (Double val : statistics.getAnimalsCnt()){
                writer.append(";").append(String.valueOf(val));
            }
            writer.append("\n");

            writer.append("grassCnt");
            for (Double val : statistics.getGrassesCnt()){
                writer.append(";").append(String.valueOf(val));
            }
            writer.append("\n");

            writer.append("freeSpaces");
            for (Double val : statistics.getFreeSpaces()){
                writer.append(";").append(String.valueOf(val));
            }
            writer.append("\n");

            writer.append("avgEnergy");
            for (Double val : statistics.getAvgEnergy()){
                writer.append(";").append(String.valueOf(val));
            }
            writer.append("\n");

            writer.append("avgChildrenCnt");
            for (Double val : statistics.getAvgChildrenCnt()){
                writer.append(";").append(String.valueOf(val));
            }
            writer.append("\n");

            writer.append("avgLifespan");
            for (Double val : statistics.getAvgLifespan()){
                writer.append(";").append(String.valueOf(val));
            }
            writer.append("\n");

            writer.append(";;;;;\n");

            writer.append("firstGenome");
            for (Integer val : genomes.get(0)){
                writer.append(";").append(String.valueOf(val));
            }
            writer.append("\n");

            writer.append("secondGenome");
            for (Integer val : genomes.get(1)){
                writer.append(";").append(String.valueOf(val));
            }
            writer.append("\n");

            writer.append("thirdGenome");
            for (Integer val : genomes.get(2)){
                writer.append(";").append(String.valueOf(val));
            }
            writer.append("\n");
        }

    }
}
