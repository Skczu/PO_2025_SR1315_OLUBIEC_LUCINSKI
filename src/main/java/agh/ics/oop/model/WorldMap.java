package agh.ics.oop.model;


import agh.ics.oop.model.exceptions.IncorrectPositionException;

import java.util.List;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place a new animal on the map.
     *
     * @param animal The animal to be placed on the map.
     * @throws  IncorrectPositionException if the animal cannot br placed. The rules for valid placement are the same as for movement.
     */
    void place(Animal animal) throws IncorrectPositionException;

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal, MoveDirection direction);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMoveTo since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    WorldElement objectAt(Vector2d position);


    /**
     * Return all elements on a WorldMap
     *
     * @return a list of worldElement objects
     */
    List<WorldElement> getElements();

    /**
     * Return lowerLeft and upperRight corners of the map.
     *
     * @return a Boundary object.
     */
    Boundary getCurrentBounds();

    /**
     * Returns the id of the map instance
     *
     * @return an int number
     */
    UUID getId();

    /**
     * Subscribes map to an event listener
     *
     * @param listener the object we are subscribing to
     */
    void subscribe(MapChangeListener listener);

    /**
     * Unsubscribes map from an event listener
     *
     * @param listener the object we are unsubscribing from
     */
    void unSubscribe(MapChangeListener listener);

    /**
     * Notifies the observer about map changes
     *
     * @param message information about event on map
     */
    void mapChanged(String message);
}
