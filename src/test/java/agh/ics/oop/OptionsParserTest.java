package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static agh.ics.oop.OptionsParser.parse;
import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {

    //args given to parse is never null so no test for this case
    @Test
    void pareEmpty() throws IllegalArgumentException{ //checks correct behavior for empty list
        //given
        String[] args = {};
        //when
        List<MoveDirection> testingParsed = parse(args);
        //then
        assertEquals(0,testingParsed.size());
    }

    @Test
    void parseValidArguments() throws IllegalArgumentException{ //checks parsing valid symbols ans skipping invalid ones
        //given
        String[] args = {"f","b","l","r"};
        //when
        List<MoveDirection> testingParsed = parse(args);
        MoveDirection[] correctlyParsed = {MoveDirection.FORWARD, MoveDirection.BACKWARD, MoveDirection.LEFT, MoveDirection.RIGHT};
        //then
        assertEquals(correctlyParsed.length,testingParsed.size()); //no useless data in parsed array
        for (int i = 0; i < correctlyParsed.length; i++) {
            assertEquals(correctlyParsed[i],testingParsed.get(i)); //valid elements
        }
    }

    @Test
    void throwsOnInvalidArguments(){ //checks parsing valid symbols ans skipping invalid ones
        //when
        String[] args1 = {"f","b","%","r"};
        String[] args2 = {"fl"};
        String[] args3 = {"F","b","l","r"};
        String[] args4 = {"b","l","r"};

        //then
        assertThrows(IllegalArgumentException.class, () -> {parse(args1);}) ;
        assertThrows(IllegalArgumentException.class, () -> {parse(args2);}) ;
        assertThrows(IllegalArgumentException.class, () -> {parse(args3);}) ;
        assertDoesNotThrow(() -> {parse(args4);}); ;
        }
    }