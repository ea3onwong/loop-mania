/**
 * Written by z5309206 Gordon Wang
 */
package test.UserInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.javatuples.Pair;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;


/**
 * This test contains tests that check each added counter to the User Interface
 * [x] Add more tests here if planning on putting more features onto UI for Milestone 3
 */
public class UserInterfaceTests {

    private int finalValue = 0;
    private int prevValue = 0;

    /**
     * Tests Health counter on UI and that it does not go below 0 or go above maximum
     */
    @Test
    public void testHealthObserver() {
        
        LoopManiaWorld world = createBasicWorld();

        world.getCharacterHealthProp().addListener(new ChangeListener<Number>() {
    
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                finalValue = newValue.intValue();
                prevValue = oldValue.intValue();
            }

        });
        // Health should be capped at 100. Since character starts at 100, the 
        // character's health should not have changed
        world.changeCharacterHealth(10);
        assertEquals(prevValue, finalValue);

        // Health decrease by 10
        world.changeCharacterHealth(-10);
        assertEquals(90, finalValue);

        // Health decrease by 9000. Health can not go below 0
        world.changeCharacterHealth(-9000);
        assertEquals(0, finalValue);

    }

    /**
     * Tests Gold counter on UI and that it does not go below 0
     */
    @Test
    public void testGoldObserver() {
        
        LoopManiaWorld world = createBasicWorld();

        world.getCharacterGoldProp().addListener(new ChangeListener<Number>() {
    
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                finalValue = newValue.intValue();
                prevValue = oldValue.intValue();
            }

        });
        // Since character starts with 5 gold, adding 10 should equal 15
        world.changeCharacterGold(10);
        assertEquals(15, finalValue);

        // Health decrease by 9999
        world.changeCharacterGold(-9999);
        assertEquals(0, finalValue);

    }

    /**
     * Tests Experience counter on UI and that it doesnt go below 0
     */
    @Test
    public void testExpObserver() {
        
        LoopManiaWorld world = createBasicWorld();

        world.getCharacterExpProp().addListener(new ChangeListener<Number>() {
    
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                finalValue = newValue.intValue();
                prevValue = oldValue.intValue();
            }

        });
        // Adds 10 exp to the character
        world.changeCharacterExp(10);
        assertEquals(10, finalValue);

    }

    /**
     * Tests Ally counter on UI
     */
    @Test
    public void testAllyObserver() {
        
        LoopManiaWorld world = createBasicWorld();

        world.getCharacterAlliesProp().addListener(new ChangeListener<Number>() {
    
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                finalValue = newValue.intValue();
                prevValue = oldValue.intValue();
            }

        });
        // Adds allies
        world.changeCharacterAllies(1);
        assertEquals(1, finalValue);

        // Assigns maximum allies
        world.changeCharacterAllies(1);
        world.changeCharacterAllies(1);
        assertEquals(3, finalValue);

        // Tries to assign more than max allies. Value should not change
        world.changeCharacterAllies(1);
        assertEquals(3, finalValue);

        world.changeCharacterAllies(77);
        assertEquals(3, finalValue);

    }


    /**
     * Tests DoggieCoin counter on UI
     */
    @Test
    public void testDoggieCoinObserver() {
        
        LoopManiaWorld world = createBasicWorld();

        world.getCharacterDoggieCoinProp().addListener(new ChangeListener<Number>() {
    
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                finalValue = newValue.intValue();
                prevValue = oldValue.intValue();
            }

        });
        // Adds a doggie coin with each loop
        for (int coin = 0; coin < 999; coin++) {
            assertEquals(coin, finalValue);
            world.changeCharacterDoggieCoin(1);
        }

    }

    /**
     * Tests Chart counter on UI
     */
    @Test
    public void testChartCounter() {
        
        LoopManiaWorld world = createBasicWorld();

        world.getDoggieSellPriceProp().addListener(new ChangeListener<Number>() {
    
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                finalValue = newValue.intValue();
                prevValue = oldValue.intValue();
            }

        });
        // Tests that the chart should change at least once
        for (int tick = 0; tick < 999999; tick++) {
            world.randomiseDoggieSellPrice();
            if (finalValue != prevValue) {
                assertTrue(finalValue != prevValue);
                return;
            }
        }
        // If the code gets here, then the chart price did not change at all
        // and thus the test fails
        assertTrue(false);

    }

    /**
     * Creates a basic LoopManiaWorld
     */
    private LoopManiaWorld createBasicWorld() {
        // Creating the world and its character
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        return world;
    }
}

