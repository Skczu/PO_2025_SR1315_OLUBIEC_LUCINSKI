package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    @Override
    public String toString(){ //returns only schematic orientation
        return switch(this) {
            case NORTH -> "N";
            case EAST -> "E";
            case SOUTH -> "S";
            case WEST -> "W";
        };
    }

    public MapDirection next(){ //returns next direction in clockwise order
        return switch(this) {
            case NORTH -> EAST;
            case EAST ->SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public MapDirection previous(){ //returns next direction in counterclockwise order
        return switch(this) {
            case NORTH -> WEST;
            case EAST ->NORTH;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
        };
    }

    public Vector2d toUnitVector(){ //returns unitary vector corresponding to direction
        return switch(this) {
            case NORTH -> new Vector2d(0,1);
            case EAST ->new Vector2d(1,0);
            case SOUTH -> new Vector2d(0,-1);
            case WEST -> new Vector2d(-1,0);
        };
    }
}
