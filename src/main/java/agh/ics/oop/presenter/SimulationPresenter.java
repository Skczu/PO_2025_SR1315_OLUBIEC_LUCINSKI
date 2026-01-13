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
    private Label moveInfoLabel;

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

    final static int CELL_WIDTH = 50;
    static final int BORDER_WIDTH = 2;
    static final int BORDER_OFFSET = BORDER_WIDTH / 2;

    public void setMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void onPauseToggle() {
        boolean isPaused = displaySimulation.togglePause();

        togglePauseButton.setText(isPaused ? "START" : "STOP");
    }

    private void drawMap(WorldMap worldMap){
        Boundary boundary = worldMap.getCurrentBounds();
        clearGrid();

        mapGrid.setWidth((boundary.upperRight().x()-boundary.lowerLeft().x()+2)*CELL_WIDTH+BORDER_OFFSET);
        mapGrid.setHeight((boundary.upperRight().y()-boundary.lowerLeft().y()+2)*CELL_WIDTH+BORDER_OFFSET);

        GraphicsContext graphics = mapGrid.getGraphicsContext2D();
        configureFont(graphics,(int) (CELL_WIDTH*0.5),Color.BLACK);
        drawFrame(graphics,BORDER_WIDTH,Color.BLUE);

        drawHeader(graphics,boundary);
        drawEntities(graphics,boundary);
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap(worldMap);
            moveInfoLabel.setText(message);

            //set statistics

            List<List<Integer>> bestGenes = displaySimulation.getSimulationStatistics().getMostCommonGenomes(3);

            livingAnimalsLabel.setText( "Living animals: " + displaySimulation.getSimulationStatistics().getAnimalsCnt().getLast());

            grassFieldsLabel.setText("Grass fields: " + displaySimulation.getSimulationStatistics().getGrassesCnt().getLast());

            unoccupiedFieldsLabel.setText("UnoUnoccupied fields: " + displaySimulation.getSimulationStatistics().getFreeSpaces().getLast());

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
        worldMap = new WorldMap(parameters.mapWidth(), parameters.mapHeight(), parameters.initialGrassAmount());
        worldMap.subscribe(this);

        displaySimulation = new Simulation(worldMap, parameters);

        //setting up initial values for statistics, choiceBox and chart on start
        String value = statsChoiceBox.getValue();
        handleChoice(value);
        statsChart.setCreateSymbols(false);

        new Thread(displaySimulation).start();
    }

    private void clearGrid() {
        GraphicsContext graphics = mapGrid.getGraphicsContext2D();
        graphics.setFill(Color.WHITE);
        graphics.fillRect(0, 0, mapGrid.getWidth(), mapGrid.getHeight());
    }

    private void configureFont(GraphicsContext graphics, int size, Color color) {
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.setFont(new Font("Arial", size));
        graphics.setFill(color);
    }

    private void drawFrame(GraphicsContext graphics, double lineWitdh, Color color){
        graphics.setStroke(color);
        graphics.setLineWidth(lineWitdh);
        graphics.strokeRect(lineWitdh/2,lineWitdh/2,mapGrid.getWidth()-lineWitdh, mapGrid.getHeight()-lineWitdh);

        for (int x = 0; x < mapGrid.getWidth() + 1; x += CELL_WIDTH) {
            graphics.strokeLine(x + BORDER_OFFSET, 0, x + BORDER_OFFSET, mapGrid.getHeight());  // BORDER_OFFSET = BORDER_WIDTH / 2
        }
        for (int y = 0; y < mapGrid.getHeight() + 1; y += CELL_WIDTH) {
            graphics.strokeLine(0, y+BORDER_OFFSET, mapGrid.getWidth(), y+BORDER_OFFSET);
        }
    }

    private void drawHeader(GraphicsContext graphics, Boundary boundary){
        //draw y\\x
        graphics.fillText("y\\x",CELL_WIDTH/2+BORDER_OFFSET,CELL_WIDTH/2+BORDER_OFFSET);

        //draws starting from upper left map corner
        int srartX =boundary.lowerLeft().x();
        //draw column headers
        for (double x = CELL_WIDTH+BORDER_OFFSET; x < mapGrid.getWidth(); x +=CELL_WIDTH) {
            graphics.fillText(String.valueOf(srartX++), x + CELL_WIDTH/2,CELL_WIDTH/2+BORDER_OFFSET);
        }

        int startY=boundary.upperRight().y();
        //row headers
        for (double y = CELL_WIDTH+BORDER_OFFSET; y < mapGrid.getHeight(); y +=CELL_WIDTH) {
            graphics.fillText(String.valueOf(startY--),CELL_WIDTH/2+BORDER_OFFSET, y + CELL_WIDTH/2);
        }
    }

    private void drawEntities(GraphicsContext graphics,Boundary boundary){
        //draws starting from upper left map corner

        int startX = boundary.lowerLeft().x(); //simulated map object X position
        int startY = boundary.upperRight().y(); //simulated map object Y position
        Vector2d position = new Vector2d(startX, startY);

        for (double y = CELL_WIDTH+BORDER_OFFSET; y < mapGrid.getHeight() ; y +=CELL_WIDTH) {

            position = new Vector2d(startX, position.y());

            for (double x = CELL_WIDTH+BORDER_OFFSET; x < mapGrid.getWidth() ; x +=CELL_WIDTH) {
                if (worldMap.isOccupied(position)) {
                    Object object = worldMap.objectAt(position);
                    if (object!=null){
                        graphics.fillText(object.toString(),x+CELL_WIDTH/2, y+CELL_WIDTH/2);
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
}
