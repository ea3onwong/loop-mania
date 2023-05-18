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
 * the randomly spawning of potion piles
 */
public class PotionPileTest {

    /**
     * Tests spawning a gold pile into the world
     */
    @Test
    public void testSpawningPotion() {

        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        // Creates a 2 long path
        orderedPath.add(Pair.with(0,0));
        orderedPath.add(Pair.with(0,1));

        LoopManiaWorld world = new LoopManiaWorld(30, 30, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        // Damages the character
        character.changeHealth(-50);
        boolean pileSpawned = false;

        // Attempts to spawn in a potion pile 999 times
        // But there should only be one potion pile spawning
        for (int tick = 0; tick < 999; tick++) {
            if (pileSpawned) {
                // If pile has been spawned, then it should always be null
                assertTrue(world.possiblySpawnPotionPile() == null);
            } else if (world.possiblySpawnPotionPile() != null) {
                pileSpawned = true;
            }
        }
        // There should only be one building
        assertTrue(world.getBuildingEntities().size() == 1);
        
        // Records the characters current HP
        int initialHP = character.getIntHP();
        // Character is not on the potion, so it should not change HP
        world.runBuildings();
        // Character should not have gained hp
        assertTrue(character.getIntHP() == initialHP);

        // Character is now on the potion, so it should change HP
        character.moveUpPath();
        world.runBuildings();
        // Character should not have gained hp
        assertTrue(character.getIntHP() > initialHP);

        // There shouldn't be anymore buildings now
        assertTrue(world.getBuildingEntities().size() == 0);
    }
}

