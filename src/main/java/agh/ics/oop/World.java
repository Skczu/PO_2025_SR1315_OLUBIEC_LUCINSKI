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

    static void main(String[] args) {

    }
}