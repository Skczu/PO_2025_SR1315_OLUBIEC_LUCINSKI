package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.enums.SimulationParameters;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    private Simulation displaySimulation;

    private WorldMap worldMap;

    private List<Double> chartValues = new ArrayList<>();

    @FXML
    private Button togglePauseButton;

    @FXML
    private Canvas mapGrid;

    //statistics labels
    @FXML
    private Label livingAnimalsLabel;

    @FXML
    private Label grassFieldsLabel;

    @FXML
    private Label unoccupiedFieldsLabel;

    @FXML
    private Label avgLifeSpanLabel;

    @FXML
    private Label avgLivingEnergyLabel;

    @FXML
    private Label avgChildrenCntLabel;

    @FXML
    private Label genomeVboxLabel;

    @FXML
    private Label firstGenome;

    @FXML
    private Label secondGenome;

    @FXML
    private Label thirdGenome;

    @FXML
    private Label displayedStatisticsLabel;

    @FXML
    private ChoiceBox<String> statsChoiceBox;

    @FXML
    private void onChoiceChanged() {
        String value = statsChoiceBox.getValue();
        handleChoice(value);
    }

    @FXML
    private LineChart<Integer,Double> statsChart;

     private int cellWidth = 27;
     private double borderWidth = 1;
     private double borderOffest = borderWidth / 2;

    public void onPauseToggle() {
        boolean isPaused = displaySimulation.togglePause();

        togglePauseButton.setText(isPaused ? "START" : "STOP");
    }

    private void drawMap(WorldMap worldMap){
        Boundary boundary = worldMap.getCurrentBounds();

        mapGrid.setWidth((boundary.upperRight().x()-boundary.lowerLeft().x()+2)* cellWidth + borderOffest);
        mapGrid.setHeight((boundary.upperRight().y()-boundary.lowerLeft().y()+2)* cellWidth + borderOffest);
        clearGrid();

        GraphicsContext graphics = mapGrid.getGraphicsContext2D();
        drawJungle(graphics);
        configureFont(graphics,(int) (cellWidth *0.5),Color.BLACK);
        drawFrame(graphics, borderWidth,Color.TAN);

        drawHeader(graphics,boundary);
        drawEntities(graphics,boundary);
    }

    @Override
    public void mapChanged(WorldMap worldMap) {
        Platform.runLater(() -> {
            drawMap(worldMap);
            //set statistics

            List<List<Integer>> bestGenes = displaySimulation.getSimulationStatistics().getMostCommonGenomes(3);

            livingAnimalsLabel.setText( "Living animals: " + displaySimulation.getSimulationStatistics().getAnimalsCnt().getLast());

            grassFieldsLabel.setText("Grass fields: " + displaySimulation.getSimulationStatistics().getGrassesCnt().getLast());

            unoccupiedFieldsLabel.setText("Unoccupied fields: " + displaySimulation.getSimulationStatistics().getFreeSpaces().getLast());

            avgLifeSpanLabel.setText("Avg life span: " + displaySimulation.getSimulationStatistics().getAvgLifespan().getLast());

            avgLivingEnergyLabel.setText("Avg living energy: " + displaySimulation.getSimulationStatistics().getAvgEnergy().getLast());

            avgChildrenCntLabel.setText("Avg children cnt: " + displaySimulation.getSimulationStatistics().getAvgChildrenCnt().getLast());

            if(!bestGenes.isEmpty()){
                firstGenome.setText("• " + bestGenes.get(0));
            }

            if(bestGenes.size()>1){
                secondGenome.setText("• " + bestGenes.get(1));
            }

            if(bestGenes.size()>2){
                thirdGenome.setText("• " + bestGenes.get(2));
            }

            updateChart();
        });
    }

    public void startSimulation(SimulationParameters parameters) {
        worldMap = new WorldMap(parameters);
        worldMap.subscribe(this);

        displaySimulation = new Simulation(worldMap, parameters);

        //setting up initial values for statistics, choiceBox and chart on start
        String value = statsChoiceBox.getValue();
        handleChoice(value);
        statsChart.setCreateSymbols(false);

        //scaling the map
        scaleMap();

        new Thread(displaySimulation).start();
    }

    private void clearGrid() {
        GraphicsContext graphics = mapGrid.getGraphicsContext2D();
        graphics.setFill(Color.rgb(255,236,201));
        graphics.fillRect(0, 0, mapGrid.getWidth(), mapGrid.getHeight());
    }

    private void drawJungle(GraphicsContext graphics){
        Vector2d lowerCorner = displaySimulation.getMap().getJungleBounds().lowerLeft();
        Vector2d upperCorner = displaySimulation.getMap().getJungleBounds().upperRight();
        int jungleHeight = (upperCorner.y()-lowerCorner.y()+1);

        graphics.setFill(Color.rgb(177,255,157));
        graphics.fillRect(0, (lowerCorner.y()+1) * cellWidth, mapGrid.getWidth(), jungleHeight * cellWidth);
    }

    private void configureFont(GraphicsContext graphics, int size, Color color) {
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.setFont(Font.font("Arial", FontWeight.BOLD, size));
        graphics.setFill(color);
    }

    private void drawFrame(GraphicsContext graphics, double lineWitdh, Color color){
        graphics.setStroke(color);
        graphics.setLineWidth(lineWitdh);
        graphics.strokeRect(lineWitdh/2,lineWitdh/2,mapGrid.getWidth()-lineWitdh, mapGrid.getHeight()-lineWitdh);

        for (int x = 0; x < mapGrid.getWidth() + 1; x += cellWidth) {
            graphics.strokeLine(x + borderOffest, 0, x + borderOffest, mapGrid.getHeight());  // BORDER_OFFSET = BORDER_WIDTH / 2
        }
        for (int y = 0; y < mapGrid.getHeight() + 1; y += cellWidth) {
            graphics.strokeLine(0, y+ borderOffest, mapGrid.getWidth(), y+ borderOffest);
        }
    }

    private void drawHeader(GraphicsContext graphics, Boundary boundary){
        //draw y\\x
        graphics.fillText("y\\x", (double) cellWidth /2+ borderOffest, (double) cellWidth /2+ borderOffest);

        //draws starting from upper left map corner
        int srartX =boundary.lowerLeft().x();
        //draw column headers
        for (double x = cellWidth + borderOffest; x < mapGrid.getWidth(); x += cellWidth) {
            graphics.fillText(String.valueOf(srartX++), x + (double) cellWidth /2, (double) cellWidth /2+ borderOffest);
        }

        int startY=boundary.upperRight().y();
        //row headers
        for (double y = cellWidth + borderOffest; y < mapGrid.getHeight(); y += cellWidth) {
            graphics.fillText(String.valueOf(startY--), (double) cellWidth /2+ borderOffest, y + (double) cellWidth /2);
        }
    }

    private void drawEntities(GraphicsContext graphics,Boundary boundary){
        //draws starting from upper left map corner

        int startX = boundary.lowerLeft().x(); //simulated map object X position
        int startY = boundary.upperRight().y(); //simulated map object Y position
        Vector2d position = new Vector2d(startX, startY);

        for (double y = cellWidth + borderOffest; y < mapGrid.getHeight() ; y += cellWidth) {

            position = new Vector2d(startX, position.y());

            for (double x = cellWidth + borderOffest; x < mapGrid.getWidth() ; x += cellWidth) {
                if (worldMap.isOccupied(position)) {
                    Object object = worldMap.objectAt(position);
                    if (object!=null){
                        if (object.getClass().equals(Animal.class)){
                            graphics.setFill(animalColor((Animal) object)); //set animal color according to its energy
                            graphics.fillOval(x + (double) cellWidth / 4, y + (double) cellWidth / 4, (double) cellWidth / 2, (double) cellWidth / 2);

                            graphics.setStroke(Color.BLACK);
                            graphics.setLineWidth((double) cellWidth / 100);
                            graphics.strokeOval(x + (double) cellWidth / 4, y + (double) cellWidth / 4, (double) cellWidth / 2, (double) cellWidth / 2);
                        }
                        else{
                            graphics.setFill(Color.GREEN); //for drawing grass;
                            graphics.fillText(object.toString(),x + (double) cellWidth / 2, y + (double) cellWidth / 2);
                        }
                        graphics.setFill(Color.BLACK); //reset to default black font
                    }
                }
                position = position.add(new Vector2d(1,0));
            }
            position = position.subtract(new Vector2d(0,1));
        }
    }


    private void handleChoice(String value) {
        chartValues = switch (value) {
            case "Living animals" -> displaySimulation.getSimulationStatistics().getAnimalsCnt();
            case "Grass fields" -> displaySimulation.getSimulationStatistics().getGrassesCnt();
            case "Unoccupied fields" -> displaySimulation.getSimulationStatistics().getFreeSpaces();
            case "Avg life span" -> displaySimulation.getSimulationStatistics().getAvgLifespan();
            case "Avg living energy" -> displaySimulation.getSimulationStatistics().getAvgEnergy();
            case "Avg living children cnt" -> displaySimulation.getSimulationStatistics().getAvgChildrenCnt();
            default -> throw new RuntimeException("Invalid option in choiceBox");
        };
    }

    private void updateChart() {
        if (chartValues == null || chartValues.isEmpty()) {
            return;
        }

        statsChart.getData().clear();

        XYChart.Series<Integer, Double> series = new XYChart.Series<>();
        series.setName("Chosen statistic value");

        for (int i = 0; i < chartValues.size(); i++) {
            series.getData().add(
                    new XYChart.Data<>(i, chartValues.get(i))
            );
        }
        statsChart.getData().add(series);
    }

    private Color animalColor(Animal animal){
        double energyPercent = Math.min(((double) animal.getEnergy())/displaySimulation.getParameters().reproductionReadyEnergy(),1);
        return Color.hsb(100*energyPercent,1,0.75);
    }

    private void scaleMap(){
        int largestDimension = Math.max(displaySimulation.getParameters().mapWidth(),displaySimulation.getParameters().mapHeight());
        //calculate displayed size from 10 to 50

        //edge case for sizes like 1,2,3
        if(largestDimension<3){
            cellWidth=200;
            borderWidth=10;
            borderOffest = borderWidth / 2;
            return;
        }

        cellWidth = (int) (425/Math.pow(largestDimension,0.9));
        borderWidth =20.0/Math.pow(largestDimension,0.9);
        borderOffest = borderWidth / 2;
    }
}
