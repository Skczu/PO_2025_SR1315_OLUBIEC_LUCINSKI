package agh.ics.oop.model;

public class Animal implements WorldElement {

    private MapDirection currentOrientation;

    private Vector2d mapPosition;

    public Animal(){
        this(new Vector2d(2,2));
    }

    public Animal(Vector2d mapPosition){
        this.currentOrientation = MapDirection.NORTH;
        this.mapPosition = mapPosition;
    }

    @Override
    public Vector2d getPosition() {
        return mapPosition;
    }

    //getter for checking orientation in tests
    public MapDirection getCurrentOrientation() {
        return currentOrientation;
    }

    @Override
    public String toString() { //returns only schematic animal position ex. N for NORTH
        return currentOrientation.toString();
    }

    @Override
    public boolean isAt(Vector2d position){
        return mapPosition.equals(position);
    }

    public void move(MoveDirection direction, MoveValidator moveValidator){
        //potential position after move
        Vector2d newPosition = mapPosition;
        switch (direction){
            case MoveDirection.RIGHT ->currentOrientation=currentOrientation.next()  ; //changes only orientation
            case MoveDirection.LEFT -> currentOrientation=currentOrientation.previous(); //changes only orientation
            case MoveDirection.FORWARD ->newPosition = newPosition.add(currentOrientation.toUnitVector());
            case MoveDirection.BACKWARD ->newPosition = newPosition.subtract(currentOrientation.toUnitVector());
        }
        //prevent leaving map - box defined atop the class
        //updates actual position only to valid values
        if (moveValidator.canMoveTo(newPosition) ){
            mapPosition = newPosition;
        }
    }
}
