package agh.ics.oop.model;

public interface WorldElement {
    /**
     * returns map position
     * @return Vector2d object
     */
    Vector2d getPosition();

    /**
     * returns boolean checking whether given position matches object actual position
     * @param position - suspected object position
     * @return ture if positions match false otherwise
     */
    boolean isAt(Vector2d position);
}
