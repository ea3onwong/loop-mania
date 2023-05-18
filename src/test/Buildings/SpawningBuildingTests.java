/**
 * Written by z5309206 Gordon Wang
 */
package test.Buildings;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Enemy.BasicEnemy;
import unsw.loopmania.Enemy.Vampire;
import unsw.loopmania.building.VampireCastleBuilding;
import unsw.loopmania.building.ZombiePitBuilding;

/**
 * This file contains 2 tests that should cover all of the edge cases for 
 * the Spawning buildings implementations
 */
public class SpawningBuildingTests {

    /**
     * Tests spawning a zombie
     */
    @Test
    public void testSpawningZombie() {
        
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        // No enemies should spawn as there arent any buildings
        ArrayList<BasicEnemy> enemies = world.runSpawnBuildings();
        assertEquals(0, enemies.size());

        // Adds a zombie pit
        ZombiePitBuilding pit = new ZombiePitBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        world.addBuildingEntity(pit);

        // Should not spawn as the building is not willing to spawn
        enemies = world.runSpawnBuildings();
        assertEquals(0, enemies.size());

        pit.setReadyToSpawn(true);
        // Should now spawn an enemy
        enemies = world.runSpawnBuildings();
        assertEquals(1, enemies.size());
    }

    /**
     * Tests spawning a vampire
     */
    @Test
    public void testSpawningVampire() {
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        // Adds a vampire castle
        VampireCastleBuilding castle = new VampireCastleBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        world.addBuildingEntity(castle);
        world.registerBuildingToCharacter(castle);

        // No enemies shall spawn
        ArrayList<BasicEnemy> enemies = world.runSpawnBuildings();
        assertEquals(0, enemies.size());

        character.increaseTotalCycles();
        character.increaseTotalCycles();
        character.increaseTotalCycles();


        // Cycle count only at 3. Should not spawn anything
        enemies = world.runSpawnBuildings();
        assertEquals(0, enemies.size());

        character.increaseTotalCycles();
        character.increaseTotalCycles();

        // Cycle count only at 5. Should spawn
        enemies = world.runSpawnBuildings();
        assertEquals(1, enemies.size());

        assertTrue(enemies.get(0) instanceof Vampire);

    }
}

