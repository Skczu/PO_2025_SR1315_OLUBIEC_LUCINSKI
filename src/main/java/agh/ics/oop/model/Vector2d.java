package agh.ics.oop.model;

import java.util.Objects;

public class Vector2d {
    private final int x;
    private final int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString(){
        return "("+x+","+y+")";
    }

    public boolean precedes(Vector2d other){ //checks if precedes given vector
        return x<=other.x && y<=other.y;
    }

    public boolean follows(Vector2d other){ //checks if follows given vector
        return x>=other.x && y>=other.y;
    }

    public Vector2d add(Vector2d other){ //sums with given vector
        return new Vector2d(x+other.x, y+other.y);
    }

    public Vector2d subtract(Vector2d other){ //subtracts given vector
        return new Vector2d(x-other.x,y- other.y);
    }

    public Vector2d upperRight(Vector2d other){ //returns vector with maximum coordinates from instance and given
        return new Vector2d(Math.max(x,other.x),Math.max(y,other.y));
    }

    public Vector2d lowerLeft(Vector2d other){ //returns vector with minimum coordinates from instance and given
        return new Vector2d(Math.min(x,other.x),Math.min(y,other.y));
    }

    public Vector2d opposite(){
        return new Vector2d(-x,-y);
    }

    @Override
    public boolean equals(Object other){
        if (this == other) //check if points to the same object
            return true;
        if (!(other instanceof Vector2d that)) //check if object is of the same class
            return false;
        return x==that.x && y==that.y; //compares values
    }

    @Override
    public int hashCode() { //fixes generating values for hash maps that use Vector2d objects
        return Objects.hash(x, y);
    }
}
