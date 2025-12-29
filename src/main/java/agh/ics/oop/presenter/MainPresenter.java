package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.navigation.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    public void onSimulationStartClicked() {
        invalidMovesMessage.setText("");
        try {
            List<MoveDirection> directions = OptionsParser.parse(textField.getText().split(" "));
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
