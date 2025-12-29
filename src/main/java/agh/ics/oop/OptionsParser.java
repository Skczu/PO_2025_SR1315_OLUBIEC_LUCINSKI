package agh.ics.oop;
import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    //uses ArrayList for efficient access while iterating
    public static List<MoveDirection> parse(String[] args) {
        List<MoveDirection> directions = new ArrayList<>(); //linked list of decoded enums for every parameter
        for (String par:args) {
            switch (par) { //parsing input argument to enum elements
                case "f" -> directions.add(MoveDirection.FORWARD);
                case "b" -> directions.add(MoveDirection.BACKWARD);
                case "l" -> directions.add(MoveDirection.LEFT);
                case "r" -> directions.add(MoveDirection.RIGHT);
                default -> throw new IllegalArgumentException(par+ " is not legal move specification");
            }
        }
        return directions;
    }
}
