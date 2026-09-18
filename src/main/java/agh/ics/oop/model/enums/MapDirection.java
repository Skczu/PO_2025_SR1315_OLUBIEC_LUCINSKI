package agh.ics.oop.model.enums;

import agh.ics.oop.model.Vector2d;

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

    /**
     * assumes that digits mean positions as 8 step clockwise directions
     * @param digit number 0-7 to be converted in a clockwise order to one of 8 directions
     * @return converted direction
     */
    public static MapDirection fromDigit(int digit){
        int rem = digit%8;
        if (rem < 0){
            rem = 8-Math.abs(rem);
        }
        return switch (rem) {
            case 0 -> NORTH;
            case 1 -> NORTHEAST;
            case 2 -> EAST;
            case 3 -> SOUTHEAST;
            case 4 -> SOUTH;
            case 5 -> SOUTHWEST;
            case 6 -> WEST;
            case 7 -> NORTHWEST;
            default -> throw new IllegalArgumentException("Invalid digit");
        };
    }

    public int toDigit(){
        return switch (this){
            case NORTH ->0;
            case NORTHEAST ->1;
            case EAST ->2;
            case SOUTHEAST ->3;
            case SOUTH ->4;
            case SOUTHWEST ->5;
            case WEST ->6;
            case NORTHWEST ->7;
        };
    }

    /**
     * rotates relative to current orientation
     * facing east and rotates south -> facing west
     * @param rotation how much will object rotate ex SOUTH -> 180 degrees
     * @return actual orientation of object
     */
    public MapDirection rotate(MapDirection rotation){
        return fromDigit((rotation.toDigit() + this.toDigit()) % 8);
    }

    /**
     * contains the logic of entities bouncing of map edges and changing their orientation by doing so
     * @return orientation after bouncing of en edge hit from each direction
     */
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

    /**
     * @return unitary vector corresponding to direction
     */
    public Vector2d toUnitVector(){ //returns
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
