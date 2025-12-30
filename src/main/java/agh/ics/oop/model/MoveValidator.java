package agh.ics.oop.model;

import agh.ics.oop.model.Vector2d;
import javafx.util.Pair;

public interface MoveValidator {

    /**
     * Indicate if any object can move to the given position.
     *
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    /**
     * Returns valid map position and animal orientation
     *after a single move forward in currently faced direction
     *
     * @param facing Current map orientation
     *
     * @param position The position checked for the movement possibility.
     * @return added position and unchanged orientation or "bounced" position and changed orientation
     */
    Pair<MapDirection,Vector2d> positionAfterMove( MapDirection facing, Vector2d position);
}
