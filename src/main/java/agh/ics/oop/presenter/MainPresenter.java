package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.navigation.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class MainPresenter {


    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    private TextField textField;

    @FXML
    private Button startButton;

    @FXML
    private Label invalidMovesMessage;

    @FXML
    private Spinner<Integer> mapWidth;

    @FXML
    private Spinner<Integer> mapHeight;

    @FXML
    private CheckBox fastAnimals;

    @FXML
    private Spinner<Integer> initialAnimalAmount;

    @FXML
    private Spinner<Integer> initialAnimalEnergy;

    @FXML
    private Spinner<Integer> dailyEnergyLoss;

    @FXML
    private Spinner<Integer> reproductionReadyEnergy;

    @FXML
    private Spinner<Integer> copulationEnergyLoss;

    @FXML
    private Spinner<Integer> initialGrassAmount;

    @FXML
    private Spinner<Integer> dailyGrassGrowth;

    @FXML
    private Spinner<Integer> grassEnergy;

    @FXML
    private Spinner<Integer> minimumMutationAmount;

    @FXML
    private Spinner<Integer> maximumMutationAmount;

    @FXML
    private Spinner<Integer> genomeLength;


    public void onSimulationStartClicked() {
        invalidMovesMessage.setText("");
        try {
            List<MoveDirection> directions = OptionsParser.parse(textField.getText().split(" "));
            List<Vector2d> startPositions = List.of(new Vector2d(3,2),new Vector2d(2,1));
            sceneManager.showSimulationWindow(directions);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            invalidMovesMessage.setText(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
