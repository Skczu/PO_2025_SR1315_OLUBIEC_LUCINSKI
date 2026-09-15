package agh.ics.oop;

import agh.ics.oop.model.Genome;

import java.io.File;
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

        String fileName = "statistics"+mapUid+".csv";
        File file = new File("statistics/"+fileName);

        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Cannot create directory: " + parent);
        }

        try (FileWriter writer = new FileWriter("statistics/"+fileName, true)) {
            List<Genome> genomes = statistics.getMostCommonGenomes(3);

            if (file.length()==0) {
                //write header
                writer.append("animalsCnt;");
                writer.append("grassCnt;");
                writer.append("freeSpaces;");
                writer.append("avgEnergy;");
                writer.append("avgChildrenCnt;");
                writer.append("firstGenome;");
                writer.append("secondGenome;");
                writer.append("thirdGenome");
                writer.append("\n");
            }

            //animals count
            writer.append(String.valueOf(statistics.getAnimalsCnt().getLast())).append(";");

            //grass count
            writer.append(String.valueOf(statistics.getGrassesCnt().getLast())).append(";");

            //free spaces count
            writer.append(String.valueOf(statistics.getFreeSpaces().getLast())).append(";");

            //avg energy
            writer.append(String.valueOf(statistics.getAvgEnergy().getLast())).append(";");

            //avg children count
            writer.append(String.valueOf(statistics.getAvgChildrenCnt().getLast())).append(";");

            //avg lifespan
            writer.append(String.valueOf(statistics.getAvgLifespan().getLast())).append(";");

            //first most popular genome
            if(!genomes.isEmpty()){
                writer.append(genomes.get(0).getGenes().toString());
            }
            else {
                writer.append("-");
            }
            writer.append(";");

            //second most popular genome
            if(genomes.size()>1){
                writer.append(genomes.get(1).getGenes().toString());
            }
            else {
                writer.append("-");
            }
            writer.append(";");

            //third most popular genome
            if(genomes.size()>2){
                writer.append(genomes.get(2).getGenes().toString());
            }
            else {
                writer.append("-");
            }
            writer.append("\n");
        }
    }
}