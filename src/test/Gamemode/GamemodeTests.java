package test.Gamemode;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.javatuples.Pair;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Boss.Doggie;
import unsw.loopmania.Enemy.Slug;
import unsw.loopmania.Gamemodes.GamemodeBerserker;
import unsw.loopmania.Gamemodes.GamemodeConfusing;
import unsw.loopmania.Gamemodes.GamemodeStandard;
import unsw.loopmania.Gamemodes.GamemodeSurvival;
import unsw.loopmania.Items.*;

/**
 * Test for functions used when purchasing or selling items.
 * Also tests the gamemodes and its impact on shopping
 */
public class GamemodeTests {
    @Test
    public void testRemoveItemFromInventory() {
        
        // Creating the world and its character
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
    
        // Player does not have sword in inventory
        assertFalse(world.removeUnequippedInventoryItemByClassName("Sword"));

        world.addUnequippedSword();

        // Player does not have shield in inventory
        assertFalse(world.removeUnequippedInventoryItemByClassName("Shield"));

        // Player does have a sword in inventory
        assertTrue(world.removeUnequippedInventoryItemByClassName("Sword"));

    }

    /**
     * Tests standard mode and its lack of restrictions
     */
    @Test
    public void testGamemodeStandard() {
        // Creating the world and its character
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        character.changeGold(99999);

        world.setGamemodeState(new GamemodeStandard());
        // All should pass as standard mode does not restrict any purchases
        for (int i = 0; i < 99; i++) {
            assertTrue(world.makePurchase("sword"));
            assertTrue(world.makePurchase("staff"));
            assertTrue(world.makePurchase("stake"));
            assertTrue(world.makePurchase("shield"));
            assertTrue(world.makePurchase("armour"));
            assertTrue(world.makePurchase("helmet"));
            assertTrue(world.makePurchase("potion"));
            character.changeGold(99999);
        }

    }

    /**
     * Tests berserker mode and its restriction on defensive equipments
     */
    @Test
    public void testGamemodeBerserker() {
        // Creating the world and its character
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        character.changeGold(99999);

        world.setGamemodeState(new GamemodeBerserker());

        // Once a defensive equipment is bought, player should not be able to
        // buy anything else
        assertTrue(world.makePurchase("shield"));
        assertFalse(world.makePurchase("shield"));
        assertFalse(world.makePurchase("armour"));
        assertFalse(world.makePurchase("helmet"));

        assertTrue(world.makePurchase("potion"));
        assertTrue(world.makePurchase("stake"));
        assertTrue(world.makePurchase("sword"));
        assertTrue(world.makePurchase("staff"));
        // After a reset, player should be able to buy another defensive equipment
        world.resetPurchases();
        assertTrue(world.makePurchase("helmet"));
        world.resetPurchases();
        assertTrue(world.makePurchase("armour"));
        world.resetPurchases();
        assertTrue(world.makePurchase("shield"));
    }

    /**
     * Tests survival mode and its restriction on potions
     */
    @Test
    public void testGamemodeSurvival() {
        // Creating the world and its character
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        character.changeGold(99999);

        world.setGamemodeState(new GamemodeSurvival());

        // Once a defensive equipment is bought, player should not be able to
        // buy anything else
        assertTrue(world.makePurchase("potion"));
        assertFalse(world.makePurchase("potion"));

        assertTrue(world.makePurchase("armour"));
        assertTrue(world.makePurchase("helmet"));
        assertTrue(world.makePurchase("shield"));
        assertTrue(world.makePurchase("stake"));
        assertTrue(world.makePurchase("staff"));
        assertTrue(world.makePurchase("sword"));

        // After a reset, the player should be able to buy another potion
        world.resetPurchases();
        assertTrue(world.makePurchase("potion"));
        assertFalse(world.makePurchase("potion"));
    }

    /**
     * Tests when inventory is full and also removes items
     */
    @Test
    public void testFullInventory() {
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        world.setGamemodeState(new GamemodeStandard());

        int capacity = 4 * 4;

        assertFalse(world.isInventoryFull());
        for (int i = 0; i < capacity; i++) {
            assertFalse(world.isInventoryFull());
            world.addUnequippedSword();
        }
        assertTrue(world.isInventoryFull());

        // Stake is not in the inventory
        assertFalse(world.removeUnequippedInventoryItemByClassName("stake"));
        assertTrue(world.isInventoryFull());

        // Sword should be removed
        assertTrue(world.removeUnequippedInventoryItemByClassName("sword"));
        assertFalse(world.isInventoryFull());

    }

    /**
     * Can't make purchases when out of money
     */
    @ParameterizedTest
    @ValueSource(strings = {"standard", "survival", "berserker"})
    public void testNotEnoughGold(String input) {
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        switch (input) {
            case "berserker":
                world.setGamemodeState(new GamemodeBerserker());
                break;
            case "survival":
                world.setGamemodeState(new GamemodeSurvival());
                break;
            default:
                world.setGamemodeState(new GamemodeStandard());
                break;
        }
        
        character.setGold(0);

        // Should all fail as the player does not have enough gold
        for (int i = 0; i < 3; i++) {
            assertFalse(world.makePurchase("sword"));
            assertFalse(world.makePurchase("staff"));
            assertFalse(world.makePurchase("stake"));
            assertFalse(world.makePurchase("shield"));
            assertFalse(world.makePurchase("armour"));
            assertFalse(world.makePurchase("helmet"));
            assertFalse(world.makePurchase("potion"));
        }

    }

    /**
     * Selling items should give lower money than buying items
     */
    @ParameterizedTest
    @ValueSource(strings = {"standard", "survival", "berserker"})
    public void testSellBuyPrice(String input) {
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        switch (input) {
            case "berserker":
                world.setGamemodeState(new GamemodeBerserker());
                break;
            case "survival":
                world.setGamemodeState(new GamemodeSurvival());
                break;
            default:
                world.setGamemodeState(new GamemodeStandard());
                break;
        }

        assertTrue(world.getSellPrice(new Sword(null, null)) < world.getBuyPrice(new Sword(null, null)));
        assertTrue(world.getSellPrice(new Stake(null, null)) < world.getBuyPrice(new Stake(null, null)));
        assertTrue(world.getSellPrice(new Staff(null, null)) < world.getBuyPrice(new Staff(null, null)));
        assertTrue(world.getSellPrice(new Armour(null, null)) < world.getBuyPrice(new Armour(null, null)));
        assertTrue(world.getSellPrice(new Shield(null, null)) < world.getBuyPrice(new Shield(null, null)));
        assertTrue(world.getSellPrice(new Helmet(null, null)) < world.getBuyPrice(new Helmet(null, null)));
    }

    /**
     * Testing that the Anduril can have Tree Stump properties
     */
    @Test
    public void testAndurilConfusingMode() {
        // Setting up world
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        world.setGamemodeState(new GamemodeConfusing());

        // Resetting the world with -1 seed means that
        // the anduril will have tree stump properties
        world.resetWorld(-1);
        // Equipping Anduril
        world.addBackendAnduril(0, 0);
        character.receiveDamage(new Doggie(null), 100);
        int characterConfusingHealth = character.getIntHP();

        world.setGamemodeState(new GamemodeStandard());
        world.resetWorld(-1);
        // Equipping Anduril
        world.addBackendAnduril(0, 0);
        character.receiveDamage(new Doggie(null), 100);
        int characterHealth = character.getIntHP();

        // With the randomised Anduril, it should save more health
        // than with nothing
        assertTrue(characterHealth < characterConfusingHealth);
    }

    /**
     * Testing that the Tree Stump can have Anduril properties
     */
    @Test
    public void testTreeStumpConfusingMode() {
        // Setting up world
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        world.setGamemodeState(new GamemodeConfusing());

        // Resetting the world with -1 seed means that
        // the tree stump will have Anduril properties
        world.resetWorld(-1);
        // Equipping TreeStump
        world.addBackendTreeStump(0, 0);
        int confusingDamage = character.attackEnemy(new Doggie(null));

        world.setGamemodeState(new GamemodeStandard());
        world.resetWorld(-1);
        // Equipping TreeStump
        world.addBackendTreeStump(0, 0);
        int normalDamage = character.attackEnemy(new Doggie(null));

        // With the randomised Tree Stump, it should deal more damage
        assertTrue(normalDamage < confusingDamage);
    }

    /**
     * Testing that the Tree Stump can have one ring properties
     * @param isConfusing
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testTreeStumpOneRingConfusingMode(boolean isConfusing) {
        // Setting up world
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        if (isConfusing) {
            world.setGamemodeState(new GamemodeConfusing());
        } else {
            world.setGamemodeState(new GamemodeStandard());
        }
        

        // Resetting the world with -1 seed means that
        // the tree stump will have One ring properties
        world.resetWorld(-1);
        world.addUnequippedTreeStump();
        character.changeHealth(-99);
        int beforeHealth = character.getIntHP();

        // Battle is successful despite the player is at critical health with a slug
        // This is because tree stump revives the player
        if (isConfusing) {
            // If the mode is confusing, the player would've survived
            assertTrue(world.individualFight(new Slug(null), 0, null));
        } else {
            // If the mode was not confusing, the player would've died
            assertFalse(world.individualFight(new Slug(null), 0, null));
            // Test ends here as player is dead
            return;
        }
        
        int afterHealth = character.getIntHP();

        // Tree stump must've increased the player's health if the gamemode was confusing
        assertTrue(beforeHealth < afterHealth);
    }

    /**
     * Testing that the Anduril can have one ring properties
     * @param isConfusing
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testAndurilOneRingConfusingMode(boolean isConfusing) {
        // Setting up world
        LoopManiaWorld world = new LoopManiaWorld(1, 2, new ArrayList<>());
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(1,1);
        orderedPath.add(pair);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        if (isConfusing) {
            world.setGamemodeState(new GamemodeConfusing());
        } else {
            world.setGamemodeState(new GamemodeStandard());
        }
        

        // Resetting the world with -3 seed means that
        // the anduril will have One ring properties
        world.resetWorld(-3);
        world.addUnequippedAnduril();
        character.changeHealth(-99);
        int beforeHealth = character.getIntHP();

        // Battle is successful despite the player is at critical health with a slug
        // This is because tree stump revives the player
        if (isConfusing) {
            // If the mode is confusing, the player would've survived
            assertTrue(world.individualFight(new Slug(null), 0, null));
        } else {
            // If the mode was not confusing, the player would've died
            assertFalse(world.individualFight(new Slug(null), 0, null));
            // Test ends here as player is dead
            return;
        }
        
        int afterHealth = character.getIntHP();

        // Tree stump must've increased the player's health if the gamemode was confusing
        assertTrue(beforeHealth < afterHealth);
    }

}
