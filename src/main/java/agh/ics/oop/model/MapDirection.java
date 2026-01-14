package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    @Override
    public String toString(){ //returns only schematic orientation
        return switch(this) {
            case NORTH -> "N";
            case NORTHEAST -> "NE";
            case EAST -> "E";
            case SOUTHEAST -> "SE";
            case SOUTH -> "S";
            case SOUTHWEST -> "SW";
            case WEST -> "W";
            case NORTHWEST -> "NW";
        };
    }

    public MapDirection next(){ //returns next direction in clockwise order
        return switch(this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    public MapDirection rotate(int n){ //rotates direction by n in clockwise order
        if (n<0) {
            n+=7;
        }
        MapDirection newDirection = this;
        for (int i = 0; i < n; i++) {
            newDirection = newDirection.next();
        }
        return newDirection;
    }

    public MapDirection bounce(){
        return switch(this) {
            case NORTH -> SOUTH;
            case NORTHEAST ->SOUTHEAST;
            case EAST ->WEST;
            case SOUTHEAST -> NORTHEAST;
            case SOUTH -> NORTH;
            case SOUTHWEST -> NORTHWEST;
            case WEST -> EAST;
            case NORTHWEST -> SOUTHWEST;
        };
    }

    public Vector2d toUnitVector(){ //returns unitary vector corresponding to direction
        return switch(this) {
            case NORTH -> new Vector2d(0,1);
            case NORTHEAST -> new Vector2d(1,1);
            case EAST ->new Vector2d(1,0);
            case SOUTHEAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0,-1);
            case SOUTHWEST -> new Vector2d(-1,-1);
            case WEST -> new Vector2d(-1,0);
            case NORTHWEST -> new Vector2d(-1,1);
        };
    }
}
