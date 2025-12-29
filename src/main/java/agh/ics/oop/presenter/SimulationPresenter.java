package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.List;

public class SimulationPresenter implements MapChangeListener {

    private WorldMap worldMap;

    private List<MoveDirection> directions;

    @FXML
    private Label moveInfoLabel;

    @FXML
    private Canvas mapGrid;

    final static int CELL_WIDTH = 50;
    static final int BORDER_WIDTH = 2;
    static final int BORDER_OFFSET = BORDER_WIDTH / 2;


    public void setMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void setDirections(List<MoveDirection> directions){
        this.directions = directions;
    }


    private void drawMap(WorldMap worldMap){
        Boundary boundary = worldMap.getCurrentBounds();
        clearGrid();

        mapGrid.setWidth((boundary.upperRight().getX()-boundary.lowerLeft().getX()+2)*CELL_WIDTH+BORDER_OFFSET);
        mapGrid.setHeight((boundary.upperRight().getY()-boundary.lowerLeft().getY()+2)*CELL_WIDTH+BORDER_OFFSET);

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
        });
    }

    public void startSimulation() {
        //assuming two animals on a grass map

        worldMap = new GrassField(5);
        worldMap.subscribe(this);
        List<Vector2d> startPositions = List.of(new Vector2d(3,2),new Vector2d(2,1));
        Simulation displaySimulation = new Simulation(startPositions,directions,worldMap);

        //no need for thread pool for one simulation
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
        int srartX =boundary.lowerLeft().getX();
        //draw column headers
        for (double x = CELL_WIDTH+BORDER_OFFSET; x < mapGrid.getWidth(); x +=CELL_WIDTH) {
            graphics.fillText(String.valueOf(srartX++), x + CELL_WIDTH/2,CELL_WIDTH/2+BORDER_OFFSET);
        }

        int startY=boundary.upperRight().getY();
        //row headers
        for (double y = CELL_WIDTH+BORDER_OFFSET; y < mapGrid.getHeight(); y +=CELL_WIDTH) {
            graphics.fillText(String.valueOf(startY--),CELL_WIDTH/2+BORDER_OFFSET, y + CELL_WIDTH/2);
        }
    }

    private void drawEntities(GraphicsContext graphics,Boundary boundary){
        //draws starting from upper left map corner

        int startX = boundary.lowerLeft().getX(); //simulated map object X position
        int startY = boundary.upperRight().getY(); //simulated map object Y position
        Vector2d position = new Vector2d(startX, startY);

        for (double y = CELL_WIDTH+BORDER_OFFSET; y < mapGrid.getHeight() ; y +=CELL_WIDTH) {

            position = new Vector2d(startX, position.getY());

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
}
