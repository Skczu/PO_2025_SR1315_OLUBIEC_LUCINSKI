package agh.ics.oop.model;


public class RectangularMap extends AbstractWorldMap{
    //width and height of map rectangle represented by lower left and upper right corners coordinates
    private final Vector2d minimalPos;
    private final Vector2d maximalPos;


    public RectangularMap(int width, int height) {
        minimalPos = new Vector2d(0,0);
        maximalPos = new Vector2d(width-1,height-1);
    }

    @Override
    public Animal objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(minimalPos,maximalPos);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && position.follows(minimalPos) && position.precedes(maximalPos);
    }
}
