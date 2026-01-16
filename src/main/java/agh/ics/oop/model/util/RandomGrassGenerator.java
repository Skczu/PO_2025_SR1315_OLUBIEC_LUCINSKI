package agh.ics.oop.model.util;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;

import java.util.*;

public class RandomGrassGenerator implements Iterable<Vector2d> {
    List<Vector2d> desertPositions = new ArrayList<>();
    List<Vector2d> junglePositions = new ArrayList<>();
    List<Vector2d> enrolledPositions = new ArrayList<>();
    Random random = new Random();

    public RandomGrassGenerator(Boundary mapBoundary, Boundary jungleBoundary, int grassCount, Set<Vector2d> excludedPositions) {
        //simplest method - generate a list of all possible positions and shuffle them

        for (int i = 0; i <= mapBoundary.upperRight().x(); i++) {
            for (int j = 0; j <= mapBoundary.upperRight().y(); j++) {
                Vector2d newPos = new Vector2d(i,j);
                if (newPos.follows(jungleBoundary.lowerLeft()) && newPos.precedes(jungleBoundary.upperRight())){
                    junglePositions.add(newPos);
                }
                else {
                    desertPositions.add(newPos);
                }
            }
            }
        junglePositions.removeAll(excludedPositions);
        desertPositions.removeAll(excludedPositions);
        Collections.shuffle(junglePositions);
        Collections.shuffle(desertPositions);

        int jungleIndex = 0;
        int desertIndex = 0;

        for (int i = 0; i < grassCount; i++) {
            boolean growInJungle = random.nextDouble() < 0.8;

            if (growInJungle && jungleIndex < junglePositions.size()) {
                enrolledPositions.add(junglePositions.get(jungleIndex++));
            }
            else if (desertIndex < desertPositions.size()) {
                enrolledPositions.add(desertPositions.get(desertIndex++));
            }
        }
    }


    @Override
    public Iterator<Vector2d> iterator() {
        return enrolledPositions.iterator();
    }
}
