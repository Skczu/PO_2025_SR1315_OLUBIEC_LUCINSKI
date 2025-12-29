package agh.ics.oop;
import agh.ics.oop.navigation.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //separated logic to manager class
        //in order to avoid breaking mvp moved navigation to SceneManager class
        new SceneManager(primaryStage).showMainWindow();
    }
}
