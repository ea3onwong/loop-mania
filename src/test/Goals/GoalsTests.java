/**
 * Written by z5309206 Gordon Wang
 */
package test.Goals;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.javatuples.Pair;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Goals.*;

/**
 * This file contains 6 tests that should cover all of the edge cases for 
 * the Goals implementation
 */
public class GoalsTests {

    /**
     * Tests a goal for gold
     */
    @Test
    public void testBasicGoldGoal() {
        
        LoopManiaWorld world = createBasicWorld();
    
        int goldNeeded = 7777;
    
        GoalsComponent goldGoal = new GoalsLeafGold(goldNeeded);

        world.setGoal(goldGoal);

        // Should fail as character does not have enough gold
        assertFalse(world.isGoalMet());

        // Changes character's gold to 7770
        world.changeCharacterGold(goldNeeded - 7);

        // Should fail as character does not have enough gold
        assertFalse(world.isGoalMet());

        // Adds 2 to character gold
        world.changeCharacterGold(2);

        // Should now pass as character has enough gold
        assertTrue(world.isGoalMet());

        // Testing printing
        assertEquals(world.printGoal(), goldGoal.printOut());
        assertTrue(world.printGoal().length() > 0);
    }

    /**
     * Tests a goal for experience
     */
    @Test
    public void testBasicExpGoal() {
        
        LoopManiaWorld world = createBasicWorld();
    
        int experienceNeeded = 9000;

        GoalsComponent expGoal = new GoalsLeafExperience(experienceNeeded);
        world.setGoal(expGoal);

        // Should fail as character does not have enough experience
        assertFalse(world.isGoalMet());

        // Changes character's GOLD to required experience
        world.changeCharacterGold(experienceNeeded);

        // Should fail as character does not have enough experience still
        assertFalse(world.isGoalMet());

        // Adds enough experiencce
        world.changeCharacterExp(experienceNeeded);

        // Should now pass as character has enough experience
        assertTrue(world.isGoalMet());

        // Testing printing
        assertEquals(world.printGoal(), expGoal.printOut());
        assertTrue(world.printGoal().length() > 0);
    }

    /**
     * Tests a goal for cycles
     */
    @Test
    public void testBasicCycleGoal() {
        
        LoopManiaWorld world = createBasicWorld();
    
        int cyclesNeeded = 200;

        GoalsComponent cycleGoal = new GoalsLeafCycle(cyclesNeeded);
        world.setGoal(cycleGoal);

        // Should not do anything
        cycleGoal.addGoal(cycleGoal);

        // Should fail as character does not have enough cycles
        assertFalse(world.isGoalMet());

        // Should fail for all 199 cycles
        for (int cycleNo = 1; cycleNo < cyclesNeeded; cycleNo++) {
            world.addCharacterCycle();
            assertFalse(world.isGoalMet());
        }

        // Adding one more cycle should have the goals met
        world.addCharacterCycle();
        assertTrue(world.isGoalMet());

        // Testing printing
        assertEquals(world.printGoal(), cycleGoal.printOut());
        assertTrue(world.printGoal().length() > 0);

    }

    /**
     * Tests a goal for cycles
     */
    @Test
    public void testBasicBossGoal() {
        
        LoopManiaWorld world = createBasicWorld();

        GoalsComponent bossGoal = new GoalsLeafBoss(0);
        world.setGoal(bossGoal);

        // Should not do anything
        bossGoal.addGoal(bossGoal);

        // Should fail as character has not killed the boss
        assertFalse(world.isGoalMet());

        world.setCharacterDoggieSlayState(true);

        // Should still fail as character has not killed all boss
        assertFalse(world.isGoalMet());

        world.setCharacterElanSlayState(true);

        // Should pass as character has killed all boss
        assertTrue(world.isGoalMet());

        // Testing printing
        assertEquals(world.printGoal(), bossGoal.printOut());
        assertTrue(world.printGoal().length() > 0);

    }

    @Test
    public void testAND() {
        
        LoopManiaWorld world = createBasicWorld();
    
        int requiredGold = 9999;
        int requiredExp = 20;

        GoalsComponent andGoal = new GoalsBranchAND();
        GoalsComponent goldGoal = new GoalsLeafGold(requiredGold);
        GoalsComponent expGoal = new GoalsLeafExperience(requiredExp);

        andGoal.addGoal(goldGoal);
        andGoal.addGoal(expGoal);

        world.setGoal(andGoal);

        // Goal should not be met at this stage
        assertFalse(world.isGoalMet());

        // Gives character enough gold, but not enough experience
        world.changeCharacterGold(requiredGold);

        // Goal should not be met at this stage
        assertFalse(world.isGoalMet());

        // Gives character enough expeience, but not enough gold
        world.changeCharacterGold(-requiredGold);
        world.changeCharacterExp(requiredExp);

        // Goal should not be met at this stage
        assertFalse(world.isGoalMet());

        // Gives character enough gold and enough experience
        world.changeCharacterGold(requiredGold);

        // Goal should be met at this stage
        assertTrue(world.isGoalMet());

        // Testing printing
        assertEquals(world.printGoal(), andGoal.printOut());
        assertNotEquals(world.printGoal(), goldGoal.printOut());
        assertTrue(world.printGoal().length() > 0);
    }

    /**
     * Tests a goal with OR
     */
    @Test
    public void testOR() {
        
        LoopManiaWorld world = createBasicWorld();
    
        int requiredGold = 9999;
        int requiredExp = 20;

        GoalsComponent orGoal = new GoalsBranchOR();
        GoalsComponent goldGoal = new GoalsLeafGold(requiredGold);
        GoalsComponent expGoal = new GoalsLeafExperience(requiredExp);

        orGoal.addGoal(goldGoal);
        orGoal.addGoal(expGoal);

        // These should not do anything or affect the following code
        goldGoal.addGoal(expGoal);
        expGoal.addGoal(goldGoal);

        world.setGoal(orGoal);

        // Goal should not be met at this stage
        assertFalse(world.isGoalMet());

        // Gives character enough gold, but not enough experience
        world.changeCharacterGold(requiredGold);

        // Goal should be met at this stage, since its gold OR exp
        assertTrue(world.isGoalMet());

        // Gives character enough experience, but not enough gold
        world.changeCharacterGold(-requiredGold);
        world.changeCharacterExp(requiredExp);

        // Goal should be met at this stage, since its gold OR exp
        assertTrue(world.isGoalMet());

        // Gives enough gold
        world.changeCharacterGold(requiredGold);

        // Goal should still be met at this stage, since its gold OR exp
        assertTrue(world.isGoalMet());

        // Testing printing
        assertEquals(world.printGoal(), orGoal.printOut());
        assertNotEquals(world.printGoal(), goldGoal.printOut());
        assertTrue(world.printGoal().length() > 0);
    }


    /**
     * Tests a goal with a combination of AND + OR
     */
    @Test
    public void testCombination() {
        
        LoopManiaWorld world = createBasicWorld();
        int requiredGold = 9999;
        int requiredExp = 20;
        int requiredCycle = 1;

        // This test will test with
        // Gold AND (Exp OR Cycle)

        GoalsComponent orGoal = new GoalsBranchOR();
        GoalsComponent andGoal = new GoalsBranchAND();

        GoalsComponent cycleGoal = new GoalsLeafCycle(requiredCycle);
        GoalsComponent goldGoal = new GoalsLeafGold(requiredGold);
        GoalsComponent expGoal = new GoalsLeafExperience(requiredExp);

        orGoal.addGoal(expGoal);
        orGoal.addGoal(cycleGoal);

        andGoal.addGoal(goldGoal);
        andGoal.addGoal(orGoal);

        world.setGoal(andGoal);

        // Goal should not be met at this stage
        assertFalse(world.isGoalMet());

        // Adds required gold
        world.changeCharacterGold(requiredGold);

        // Goal should not be met at this stage as it needs Gold AND (Exp OR Cycle)
        assertFalse(world.isGoalMet());

        // Adds required exp
        world.changeCharacterExp(requiredExp);

        // Goal should be met at this stage as it needs Gold AND (Exp OR Cycle)
        // Gold and Exp are met, so should return true
        assertTrue(world.isGoalMet());

        // Removes required exp and adds enough cycles
        world.changeCharacterExp(-requiredExp);

        // Goal should not be met anymore
        assertFalse(world.isGoalMet());

        // Goal should now be met again
        world.addCharacterCycle();

        assertTrue(world.isGoalMet());

        // Testing printing
        assertEquals(world.printGoal(), andGoal.printOut());
        assertNotEquals(world.printGoal(), orGoal.printOut());
        assertTrue(world.printGoal().length() > 0);

    }

    /**
     * Creates a basic world to test in
     * @return
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

