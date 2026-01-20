package agh.ics.oop.presenter;

import agh.ics.oop.model.enums.SimulationParameters;
import agh.ics.oop.navigation.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainPresenter {


    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

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

    @FXML
    private Spinner<Integer> fastAnimalsEnergyThreshold;

    @FXML
    private Spinner<Integer> fastAnimalsSpeedIncreaseThreshold;

    @FXML
    private Spinner<Integer> fastAnimalsMaxSpeed;


    public void onSimulationStartClicked() {
        invalidMovesMessage.setText("");

        SimulationParameters simulationParameters = new SimulationParameters(
                mapWidth.getValue(),
                mapHeight.getValue(),
                fastAnimals.isSelected(),
                initialAnimalAmount.getValue(),
                initialAnimalEnergy.getValue(),
                dailyEnergyLoss.getValue(),
                reproductionReadyEnergy.getValue(),
                copulationEnergyLoss.getValue(),
                initialGrassAmount.getValue(),
                dailyGrassGrowth.getValue(),
                grassEnergy.getValue(),
                minimumMutationAmount.getValue(),
                maximumMutationAmount.getValue(),
                genomeLength.getValue(),
                fastAnimalsEnergyThreshold.getValue(),
                fastAnimalsSpeedIncreaseThreshold.getValue(),
                fastAnimalsMaxSpeed.getValue()
        );

        try {
            sceneManager.showSimulationWindow(simulationParameters);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            invalidMovesMessage.setText(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
