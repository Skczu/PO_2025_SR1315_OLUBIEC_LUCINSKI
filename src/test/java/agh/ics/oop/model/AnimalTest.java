package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {

    static final Vector2d POSITION = new Vector2d(1,1);
    static final Genome GENOME = new Genome(List.of(1));
    static final int START_ENERGY = 10;

    @Test
    void checksPosition(){
        //when
        Vector2d pos1 = new Vector2d(1,2);
        Vector2d pos2 = new Vector2d( 1,1);
        Animal tigger = new Animal(POSITION,GENOME,START_ENERGY);

        //then
        assertFalse(tigger.isAt(pos1));
        assertTrue(tigger.isAt(pos2));
    }


    @Test
    void eatsCorrectly(){
        //given
        Animal eeyore = new Animal(POSITION,GENOME,START_ENERGY);
        Grass grass1 = new Grass(new Vector2d(1,1));
        Grass grass2 = new Grass(new Vector2d(0,1));

        //when
        eeyore.eat(grass1,1);
        eeyore.eat(grass2,100);

        //then
        assertEquals(11,eeyore.getEnergy());
    }

    @Test
    void setsCorrectSpeed(){
        //given
        Animal piglet = new Animal(POSITION,GENOME,START_ENERGY);
        Animal pooh = new Animal(POSITION,GENOME,2);
        Animal rabbit = new Animal(POSITION,GENOME,0);
        Animal owl = new Animal(POSITION,GENOME,0);

        //when
        pooh.setSpeed(3,4,10);
        piglet.setSpeed(3,4,10);
        rabbit.setSpeed(3,4,10);
        owl.setSpeed(3,4,10);

        //then
        assertEquals(3,piglet.getSpeed());
        assertEquals(1,pooh.getSpeed());
        assertEquals(1,rabbit.getSpeed());
        assertEquals(1,owl.getSpeed());
    }


    @Test
    void comparesToOther(){
        Animal animal1 = new Animal(POSITION,GENOME,START_ENERGY);
        Animal animal2 = new Animal(POSITION,GENOME,100);
        Animal animal3 = new Animal(POSITION,GENOME,START_ENERGY);

        //when
        animal2.setChildrenCnt(10);
        animal3.liveOneDay();

        //then
        assertTrue(animal1.compareTo(animal2)>0);
    }

    @Test
    void rotatesCorrectly(){
        //given
        Animal piglet = new Animal(POSITION,GENOME,START_ENERGY);
        Animal kanga = new Animal(POSITION,new Genome(List.of(3,-3)),START_ENERGY);

        //when
        piglet.rotate();
        kanga.rotate();
        kanga.rotate();

        //then
        assertEquals(MapDirection.NORTHEAST,piglet.getCurrentOrientation());
        assertEquals(MapDirection.NORTH,kanga.getCurrentOrientation());
    }
}
