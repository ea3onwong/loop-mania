package test.ResettingGame;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Gamemodes.GamemodeConfusing;
import unsw.loopmania.building.ZombiePitBuilding;
import unsw.loopmania.Character;


public class ResettingTests {

    @Test
    public void testReset() {
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        // Creates a 3 long path
        orderedPath.add(Pair.with(0,0));
        orderedPath.add(Pair.with(0,1));
        orderedPath.add(Pair.with(0,2));

        LoopManiaWorld world = new LoopManiaWorld(30, 30, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        world.setGamemodeState(new GamemodeConfusing());

        // Testing begins below

        while (world.getEnemies().size() == 0) {
            // Spawn enemies
            world.spawnVampire(0, 0);
            world.possiblySpawnEnemies();
        }

        // There should be enemies spawned on the map
        assertTrue(world.getEnemies().size() > 0);

        // Adds a building onto the map
        world.addBuildingEntity(new ZombiePitBuilding(new SimpleIntegerProperty(20), new SimpleIntegerProperty(20)));
        assertTrue(world.getBuildingEntities().size() > 0);

        world.changeCharacterDoggieCoin(1);
        assertTrue(world.getCharacterDoggieCoinProp().get() == 1);

        world.changeCharacterHealth(-10);
        assertTrue(world.getCharacterHealthProp().get() != 100);

        // Player should've moved
        world.runTickMoves();
        assertTrue(character.getCurrentPositionIndex() != 0);

        world.resetWorld(System.currentTimeMillis());
        // Everything should be reset, so no buildings and enemies
        assertTrue(world.getEnemies().size() == 0);
        assertTrue(world.getBuildingEntities().size() == 0);
        assertTrue(world.getCharacterDoggieCoinProp().get() == 0);
        assertTrue(world.getCharacterHealthProp().get() == 100);
        assertTrue(character.getCurrentPositionIndex() == 0);
    }

}


