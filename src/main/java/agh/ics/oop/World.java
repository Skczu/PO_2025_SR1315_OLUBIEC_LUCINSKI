package agh.ics.oop;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.ConsoleMapDisplay;

import java.util.ArrayList;
import java.util.List;

//doesn't use Polish signs for clarity reasons
public class World {
    //run prints animal movements determined by Enum directions
    public static void run(List<MoveDirection> directions ){
        for (MoveDirection animalDir: directions) {
            String moved=switch(animalDir){ //moved is displayed as the output
                case FORWARD -> "Zwierzak idzie do przodu";
                case BACKWARD -> "Zwierzak idzie do tylu";
                case LEFT -> "Zwierzak idzie w lewo";
                case RIGHT -> "Zwierzak idzie w prawo";
            };
            System.out.println(moved);
        }
    }

//    static void main(String[] args) {
//        List<MoveDirection> inputDirections;
//        try {
//            inputDirections = OptionsParser.parse(args);
//        }
//        catch (IllegalArgumentException e){
//            e.printStackTrace();
//            return;
//        }
//
//        System.out.println("system wystartowal");
//        String[] simArgs = {"f", "b", "r", "l", "b", "b","b", "b","f","f","f","r","l","f","f"};
//        List<MoveDirection> directions = OptionsParser.parse(simArgs);
//
//        //displayer for smaller maps
//        ConsoleMapDisplay fewDisplayer = new ConsoleMapDisplay();
//
//        //Simulation run validation, updated for using rectangular map class
//        List<Vector2d> positionsRect = List.of(new Vector2d(2,2), new Vector2d(3,4));
//        RectangularMap rectMap = new RectangularMap(5,5);
//        rectMap.subscribe(fewDisplayer); //registering new observer for my map
//        Simulation heffalumpsForest = new Simulation(positionsRect, inputDirections,rectMap);
//        heffalumpsForest.run();
//
//        //checking the GrassField class checking the same arguments as for lab4
//        List<Vector2d> positionsGrass = List.of(new Vector2d(2,2), new Vector2d(3,4));
//        GrassField grassMap = new GrassField(10);
//        grassMap.subscribe(fewDisplayer); //registering new observer for my map
//        Simulation hundredMileForest = new Simulation(positionsGrass,inputDirections,grassMap);
//        hundredMileForest.run();
//
//        //checking Simulation engine class
//        SimulationEngine myEngine = new SimulationEngine(List.of(heffalumpsForest,hundredMileForest));
//        //myEngine.runAsync();
//        try {
//            myEngine.awaitSimulationsEnd();
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
//
//        //crating 1000 simulations of two animals with move parameters from IO
//        List<Simulation> manySimulations = new ArrayList<>();
//        ConsoleMapDisplay manyDisplayer = new ConsoleMapDisplay();
//        List<Vector2d> iPositions = List.of(new Vector2d(1, 2), new Vector2d(3, 4));
//
//        for (int i = 0; i < 1000; i++) {
//            //fixed placing positions for ensuring place possibility
//
//            GrassField iMap = new GrassField(10); //using only grass maps for the lack of move boundaries
//            iMap.subscribe(manyDisplayer); //registering to observer for my map
//            manySimulations.add(new Simulation(iPositions,directions,iMap));
//        }
//
//        //runs simulation on the same maps twice (using Thread list and ExecutorService threadPool)
//        //so twice as many updates on the same listener are expected
//        SimulationEngine manyEngine = new SimulationEngine(manySimulations);
//        manyEngine.runAsync(); //slower because immediately creates 1000 threads which occupy a lot of resources while waiting for execution [perhaps better for a 1000 core cpu ;) ]
//        try {
//            manyEngine.awaitSimulationsEnd();
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
//
//        SimulationEngine manyPoolEngine = new SimulationEngine(manySimulations);
//        manyPoolEngine.runAsyncInThreadPool();  //faster because doesn't immediately create 1000 threads, just the four that a statistical CPU can handle a time
//        try {
//            manyPoolEngine.awaitSimulationsEnd();
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
//
//        System.out.println(new RectangularMap(5,5));
//        System.out.println("system zakonczyl dzialanie");
//    }
}