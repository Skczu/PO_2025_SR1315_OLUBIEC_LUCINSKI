package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    List<Vector2d> possiblePositions = new ArrayList<>();
    List<Vector2d> enrolledPositions = new ArrayList<>();

    public RandomPositionGenerator(int maxWidth, int maxHeight, int animalCount) {
        for (int i = 0; i < maxWidth+1; i++) {
            for (int j = 0; j < maxHeight+1; j++) {
                possiblePositions.add(new Vector2d(i,j));
            }
        }

        Collections.shuffle(possiblePositions);

        for (int i = 0; i < animalCount; i++) {
            //quants i for cases with more animals than width*height
            enrolledPositions.add(possiblePositions.get(i% possiblePositions.size()));
        }
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return enrolledPositions.iterator();
    }
}