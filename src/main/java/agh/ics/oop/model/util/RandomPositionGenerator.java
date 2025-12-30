package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2d> {

    List<Vector2d> possiblePositions = new ArrayList<>();
    List<Vector2d> enrolledGrassPositions = new ArrayList<>();
    //private final int grassCount;

    public RandomPositionGenerator(int maxWidth, int maxHeight, int grassCount) {
        //simplest method - generate a list of all possible positions and shuffle them
        for (int i = 0; i < maxWidth+1; i++) {
            for (int j = 0; j < maxHeight+1; j++) {
                possiblePositions.add(new Vector2d(i,j));
            }
        }
        Collections.shuffle(possiblePositions);
        for (int i = 0; i < grassCount; i++) {
            enrolledGrassPositions.add(possiblePositions.get(i));
        }
    }


    @Override
    public Iterator<Vector2d> iterator() {
        return enrolledGrassPositions.iterator();
    }
}