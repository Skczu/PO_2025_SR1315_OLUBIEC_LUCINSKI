package agh.ics.oop.navigation;

import agh.ics.oop.model.enums.SimulationParameters;
import agh.ics.oop.presenter.MainPresenter;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SceneManager {

    private final Stage mainStage;

    public SceneManager(Stage stage) {
        this.mainStage = stage;
    }

    public void showMainWindow() throws Exception {
        FXMLLoader loader = new FXMLLoader(); // FXML loader initiation

        loader.setLocation(getClass().getClassLoader().getResource("main.fxml"));
        BorderPane viewRoot = loader.load();

        MainPresenter presenter = loader.getController();
        presenter.setSceneManager(this);

        Stage mainStage = new Stage();

        configureStage(mainStage,viewRoot,"Simulation app",new Image("genome.png"));
    }


    public void showSimulationWindow(SimulationParameters parameters) throws Exception {
        FXMLLoader loader = new FXMLLoader(); // FXML loader initiation

        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        SimulationPresenter presenter = loader.getController();

        Stage simulationStage = new Stage();

        simulationStage.setX(mainStage.getX() + Math.random()*100 ); //window offest for clarity
        simulationStage.setY(mainStage.getY()  + Math.random()*100); //using random values for less overlapping


        configureStage(simulationStage,viewRoot,"Simulation", new Image("genome.png"));

        presenter.startSimulation(parameters);
    }


    private void configureStage(Stage primaryStage, BorderPane viewRoot, String title, Image image) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);

        // window configuration
        primaryStage.setTitle(title);
        primaryStage.getIcons().add(image);
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        primaryStage.show();
    }
}
