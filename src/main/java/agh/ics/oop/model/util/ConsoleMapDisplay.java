package agh.ics.oop.model.util;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {

    private int updates=0;

    //when the method is not called in a synchronized way often dhe displayer cam be called to again before it finishes drawing the map
    //therefore often may map update information are displayed simultaneously before drawing maps and the update counter is mishandled
    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
            System.out.println("Map ID: " + worldMap.getId());
            System.out.println("No. of update: " + updates++);
            System.out.println(message);
            System.out.println(worldMap);

    }
}
