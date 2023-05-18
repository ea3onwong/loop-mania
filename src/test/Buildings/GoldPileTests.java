/**
 * Written by z5309206 Gordon Wang
 */
package test.Buildings;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.javatuples.Pair;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

/**
 * This file contains a test that should cover all of the edge cases for 
 * the randomly spawning of gold piles
 */
public class GoldPileTests {

    /**
     * Tests spawning a gold pile into the world
     */
    @Test
    public void testSpawningGold() {

        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        // Creates a 2 long path
        orderedPath.add(Pair.with(0,0));
        orderedPath.add(Pair.with(0,1));

        LoopManiaWorld world = new LoopManiaWorld(30, 30, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        boolean pileSpawned = false;

        // Attempts to spawn in a gold pile 999 times
        // But there should only be one gold pile spawning
        for (int i = 0; i < 9999; i++) {
            if (pileSpawned) {
                // If pile has been spawned, then it should always be null
                assertTrue(world.possiblySpawnGoldPiles() == null);
            } else if (world.possiblySpawnGoldPiles() != null) {
                pileSpawned = true;
            }
        }
        // There should only be one building
        assertTrue(world.getBuildingEntities().size() == 1);
        
        // Records the characters current gold
        int initialGold = character.getIntGold();
        // Character is not on the pile, so their gold should not change
        world.runBuildings();
        // Character should have gained gold
        assertTrue(character.getIntGold() == initialGold);

        // Character is now on the pile, so the gold should change
        character.moveUpPath();
        world.runBuildings();
        // Character should have gained gold
        assertTrue(character.getIntGold() > initialGold);

        // There shouldn't be anymore buildings now
        assertTrue(world.getBuildingEntities().size() == 0);
    }
}

