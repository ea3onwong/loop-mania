/**
 * Written by z5309206 Gordon Wang
 */
package test.Items;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.javatuples.Pair;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Items.*;


/**
 * This test file contains tests for enabling rare items
 */
public class JSONEnablingItemsTest {

    @Test
    public void testItemBeingEnabled() {
        LoopManiaWorld world = createBasicWorld();
        // Enabling armour should do nothing as it is not rare
        world.enableRareItem(new Armour(null, null));
        assertFalse(world.isThisRareItemEnabled(new Anduril(null, null)));

        // Anduril is enabled
        world.enableRareItem(new Anduril(null, null));
        assertTrue(world.isThisRareItemEnabled(new Anduril(null, null)));

        // One Ring is enabled
        assertFalse(world.isThisRareItemEnabled(new TheOneRing(null, null)));
        world.enableRareItem(new TheOneRing(null, null));
        assertTrue(world.isThisRareItemEnabled(new TheOneRing(null, null)));

        // Tree Stump is enabled
        assertFalse(world.isThisRareItemEnabled(new TreeStump(null, null)));
        world.enableRareItem(new TreeStump(null, null));
        assertTrue(world.isThisRareItemEnabled(new TreeStump(null, null)));
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

