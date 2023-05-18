/**
 * Written by z5309206 Gordon Wang
 */
package test.Enemies;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;

import unsw.loopmania.Card;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.Boss.*;
import unsw.loopmania.Exceptions.CharacterIsDead;
import unsw.loopmania.building.*;
import unsw.loopmania.Enemy.*;
import unsw.loopmania.Enemy.EnemyMoves.*;

/**
 * This file contains tests that should cover all of the edge cases for 
 * the Zombies, Vampires, Slugs, Elan, Doggie and Thief
 */
public class EnemiesTests {

    /**
     * Character should lose health against zombie and gain experience and gold
     */
    @Test
    public void testBattleZombie() {
        
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        world.addBackendSword(0, 0);

        int initialGold = world.getCharacterGoldProp().get();
        int initialExp = world.getCharacterExpProp().get();
        int initialCharacterHealth = character.getIntHP();

        // Adds a zombie pit
        ZombiePitBuilding pit = new ZombiePitBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        world.addBuildingEntity(pit);
        pit.setReadyToSpawn(true);
        world.runSpawnBuildings();

        try {
            world.runBattles(System.currentTimeMillis());
        } catch (Exception e) {
            // Fails the test if the character dies
            assertFalse(true);
        }

        // Character should've lost health and have different gold and exp
        assertNotEquals(initialCharacterHealth, character.getIntHP());
        assertTrue(initialGold < character.getIntGold());
        assertTrue(initialExp < character.getIntExp());
    }

    /**
     * Character should lose health against vampire
     */
    @Test
    public void testBattleVampire() {
        
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        world.addBackendSword(0, 0);

        int initialCharacterHealth = character.getIntHP();

        // Adds a Vampire castle
        VampireCastleBuilding castle = new VampireCastleBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        world.addBuildingEntity(castle);
        castle.setReadyToSpawn(true);
        world.runSpawnBuildings();

        try {
            world.runBattles(System.currentTimeMillis());
        } catch (CharacterIsDead e) {
            // Character may die here
            assertTrue(character.getIntHP() == 0);
        }

        // Character should've lost health and have different gold and exp
        assertNotEquals(initialCharacterHealth, character.getIntHP());
    }

    /**
     * Character should not lose health if nothing was faught
     */
    @Test
    public void testBattleNothing() {
        
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        
        int initialGold = world.getCharacterGoldProp().get();
        int initialExp = world.getCharacterExpProp().get();
        int initialCharacterHealth = character.getIntHP();

        try {
            world.runBattles(System.currentTimeMillis());
        } catch (Exception e) {
            // Fails the test if the character dies
            assertFalse(true);
        }

        // Character shouldn't lose health and have the same gold and exp
        assertEquals(initialCharacterHealth, character.getIntHP());
        assertEquals(initialGold, character.getIntGold());
        assertEquals(initialExp, character.getIntExp());
    }

    /**
     * Tests that all enemies can move around the map
     */
    @ParameterizedTest
    @ValueSource(strings = {"zombie", "vampire", "slug", "doggie", "elan", "thief"})
    public void movingEnemy(String enemyName) {
        
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pairA = Pair.with(0,0);
        Pair<Integer, Integer> pairB = Pair.with(0,1);
        Pair<Integer, Integer> pairC = Pair.with(1,1);
        orderedPath.add(pairA);
        orderedPath.add(pairB);
        orderedPath.add(pairC);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        BasicEnemy basicEnemy;
        switch (enemyName) {
            case "zombie":
                basicEnemy = new Zombie(position);
                break;
            case "vampire":
                basicEnemy = new Vampire(position);
                break;
            case "doggie":
                basicEnemy = new Doggie(position);
                break;
            case "elan":
                basicEnemy = new ElanMuske(position);
                break;
            case "thief":
                basicEnemy = new Thief(position, 100);
                break;
            default:
                // Defaults to a normal slug enemy
                basicEnemy = new Slug(position);
                break;
            
        }
        int xCoord = basicEnemy.getX();
        int yCoord = basicEnemy.getY();

        basicEnemy.moveDownPath();

        // Since the path is from 0 0 to 0 1, then x coords stays the same while y coords differ
        assertEquals(xCoord, basicEnemy.getX());
        assertNotEquals(yCoord, basicEnemy.getY());

        // Back to original position
        basicEnemy.moveUpPath();
        assertEquals(xCoord, basicEnemy.getX());
        assertEquals(yCoord, basicEnemy.getY());

        int seed = 1;
        basicEnemy.move(seed);

        if (basicEnemy instanceof Zombie) {
            // Zombies normally stand still.
            // Seed of 1 always has the Zombie stay still
            assertEquals(yCoord, basicEnemy.getY());
        } else {
            // Seed of 1 moves normal enemies
            assertNotEquals(yCoord, basicEnemy.getY());
        }

    }

    /**
     * Tests that the enemies perform the right moves
     */
    @Test
    public void testEnemyAttack() {
        // Creating path for Zombie/Vampire
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pairA = Pair.with(0,0);
        Pair<Integer, Integer> pairB = Pair.with(0,1);
        Pair<Integer, Integer> pairC = Pair.with(1,1);
        orderedPath.add(pairA);
        orderedPath.add(pairB);
        orderedPath.add(pairC);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        Zombie zombie = new Zombie(position);
        Vampire vampire = new Vampire(position);
        Slug slug = new Slug(position);

        // Tests that the attack is either a crit hit or a normal hit
        BasicMove move;
        for (int i = 0; i < 100; i++) {
            move = vampire.attack(System.currentTimeMillis());
            assertTrue(move instanceof VampireCritHit || move instanceof VampireHit);
        }

        // Tests that the attack is either a crit hit or a normal hit
        for (int i = 0; i < 100; i++) {
            move = zombie.attack(System.currentTimeMillis());
            assertTrue(move instanceof ZombieCritHit || move instanceof ZombieHit);
        }
        

        // Slugs only have one move
        assertTrue(slug.attack(System.currentTimeMillis()) instanceof SlugHit);

    }

    /**
     * Tests that the enemy are hierarchied via their drops and battle/support radii
     */
    @Test
    public void testEnemyGoldExpDropsHierarchy() {
        // Creating path for Zombie/Vampire
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pairA = Pair.with(0,0);
        Pair<Integer, Integer> pairB = Pair.with(0,1);
        Pair<Integer, Integer> pairC = Pair.with(1,1);
        orderedPath.add(pairA);
        orderedPath.add(pairB);
        orderedPath.add(pairC);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        Zombie zombie = new Zombie(position);
        Vampire vampire = new Vampire(position);
        Slug slug = new Slug(position);

        int slugExp = slug.dropExp(), slugGold = slug.dropGold();
        int zombieExp = zombie.dropExp(), zombieGold = zombie.dropGold();
        int vampireExp = vampire.dropExp(), vampireGold = vampire.dropGold();

        int slugBattleRadius = slug.getBattleRadius(), slugSupportRadius = slug.getSupportRadius();
        int zombieBattleRadius = zombie.getBattleRadius(), zombieSupportRadius = zombie.getSupportRadius();
        int vampireBattleRadius = vampire.getBattleRadius(), vampireSupportRadius = vampire.getSupportRadius();

        // Slug should drop lower stuff than zombie and zombie lower than vampire
        assertTrue(slugExp < zombieExp);
        assertTrue(slugExp < vampireExp);
        assertTrue(slugGold < zombieGold);
        assertTrue(zombieGold < vampireGold);

        // Support radius hierarchy. Vampire should have the most support radius
        assertTrue(slugSupportRadius < vampireSupportRadius);
        assertTrue(zombieSupportRadius < vampireSupportRadius);

        // Battle radius hierarchy. Slug and zombie should have the smallest battle radius
        assertTrue(slugBattleRadius < vampireBattleRadius);
        assertTrue(zombieBattleRadius < vampire.getBattleRadius());
    }

    /**
     * Tests that the support radius works and that the enemies actually can find
     * each other for support
     */
    @Test
    public void testSupportRadius() {
        // Creating path for Zombie/Vampire
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pairA = Pair.with(0,0);
        Pair<Integer, Integer> pairC = Pair.with(29,29);
        orderedPath.add(pairA);
        orderedPath.add(pairC);

        LoopManiaWorld world = new LoopManiaWorld(30, 30, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        // Vampire is spawned far away from zombie
        // Since there is limited paths, they will always spawn in the exact location
        // specified by the passed in x and y, eliminating any randomness
        BasicEnemy zombie = world.spawnZombie(29, 29);
        BasicEnemy vampire = world.spawnVampire(0, 0);

        // The only supportive enemy should be the zombie/vampire itself
        assertTrue(world.getSupportiveEnemies(zombie).size() == 1);
        assertTrue(world.getSupportiveEnemies(vampire).size() == 1);

        vampire.moveUpPath();

        // With the vampire closer, zombie should have itself and the vampire as
        // supports
        assertTrue(world.getSupportiveEnemies(zombie).size() == 2);
        assertTrue(world.getSupportiveEnemies(vampire).size() == 2);

        // Spawn in another vampire and now there should be more support
        world.spawnVampire(29, 29);
        assertTrue(world.getSupportiveEnemies(vampire).size() == 3);
    }

    /**
     * Tests that enemies can kill the character
     */
    @Test
    public void testCharacterKill() {

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pairA = Pair.with(0,0);
        orderedPath.add(pairA);

        LoopManiaWorld world = new LoopManiaWorld(30, 30, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);
        // Spawns five vampires, which should kill the player
        world.spawnVampire(0, 0);
        world.spawnVampire(0, 0);
        world.spawnVampire(0, 0);
        world.spawnVampire(0, 0);
        world.spawnVampire(0, 0);
        assertTrue(character.getIntHP() != 0);
        Exception thrownException = null;
        try {
            world.runBattles(System.currentTimeMillis());
        } catch (Exception e) {
            // Stores exception inside thrownException
            thrownException = e;
        }
        // Asserts that the exception is that the character has died
        assertTrue(thrownException instanceof CharacterIsDead);
        assertTrue(character.getIntHP() == 0);
    }

    /**
     * Tests that the vampire will never be inside the campfire's radius
     */
    @Test
    public void testVampireMoveFromCampfire() {
        // Creating path
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        orderedPath.add(Pair.with(0,1));
        orderedPath.add(Pair.with(0,2));
        orderedPath.add(Pair.with(0,3));
        orderedPath.add(Pair.with(0,4));
        orderedPath.add(Pair.with(0,5));
        orderedPath.add(Pair.with(0,6));
        orderedPath.add(Pair.with(0,7));
        orderedPath.add(Pair.with(0,8));
        orderedPath.add(Pair.with(0,9));
        orderedPath.add(Pair.with(0,10));

        LoopManiaWorld world = new LoopManiaWorld(30, 30, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        // Spawns vampire and campfire on the world
        BasicEnemy vampire = world.spawnVampire(0, 8);
        CampfireBuilding campfire = new CampfireBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(10));
        world.addBuildingEntity(campfire);
        
        double distance = Math.sqrt( Math.pow((vampire.getX() - campfire.getX()), 2) +  Math.pow((vampire.getY() - campfire.getY()), 2) );
        
        world.runTickMoves();

        for (int tick = 0; tick < 99; tick++) {
            world.runTickMoves();
        }
        // Vampire should not be in radius
        distance = Math.sqrt( Math.pow((vampire.getX() - campfire.getX()), 2) +  Math.pow((vampire.getY() - campfire.getY()), 2) );
        assertTrue(distance > campfire.getRadius());
        
    }

    /**
     * Tests loot drops for slug vampire and zombie
     * Tests that they drop the right classes
     * @param enemyName
     */
    @ParameterizedTest
    @ValueSource(strings = {"slug", "vampire", "zombie", "doggie", "elan"})
    public void testItemDrops(String enemyName) {
        BasicEnemy enemy;
        switch (enemyName) {
            case "vampire":
                enemy = new Vampire(null);
                break;
            case "zombie":
                enemy = new Zombie(null);
                break;
            case "doggie":
                enemy = new Doggie(null);
                break;
            case "elan":
                enemy = new ElanMuske(null);
                break;
            default:
                enemy = new Slug(null);
                break;
        }
        Object loot;
        for (int i = 0; i < 999; i++) {
            loot = enemy.dropBuildingLoot();
            if (loot != null) {
                assertTrue(loot instanceof Card);
            }
            loot = enemy.dropItemLoot();
            if (loot != null) {
                assertTrue(loot instanceof StaticEntity);
            }
            

        }

        if (enemy instanceof Boss) {
            // Testing boss loot
            Boss boss = (Boss) enemy;
            assertTrue(boss.dropCrypto() > 0);
        }
    }

    /**
     * Tests that a critical bite on the player should decrease the ally count and
     * adds another zombie onto the field
     */
    @Test
    public void testZombieCritBite() {
        // Creating the world
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pairA = Pair.with(0,0);
        orderedPath.add(pairA);

        LoopManiaWorld world = new LoopManiaWorld(30, 30, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        assertTrue(character.getIntAllies() == 0);

        assertTrue(world.getEnemies().size() == 0);
        assertFalse(world.spawnZombie(0, 0) == null);
        assertTrue(world.getEnemies().size() == 1);

        long seed = 1;

        assertDoesNotThrow(() -> world.runBattles(seed));

        // Seed of 1 always lands a critical bite. However, nothing is spawned
        // as the player does not have any allies
        assertTrue(world.getEnemies().size() == 0);

        // Respawns Zombie
        assertFalse(world.spawnZombie(0, 0) == null);
        assertTrue(world.getEnemies().size() == 1);
        // Changes allies
        character.changeHealth(999999);
        character.changeAlly(1);
        assertTrue(character.getIntAllies() == 1);

        // Critical bite lands, and the player shall lose an ally and a zombie is spawned
        // the previous zombie is still dead, so the size does not change
        assertDoesNotThrow(() -> world.runBattles(seed));
        // A new zombie in the world
        assertTrue(world.getEnemies().size() == 1);
        // Ally has depleted
        assertTrue(character.getIntAllies() == 0);

        
    }

    /**
     * Tests the spawning of slugs and that it should cap at 2
     */
    @Test
    public void testSpawningSlugs() {
        
        LoopManiaWorld world = createBasicWorld();

        for (int i = 0; i < 999; i++) {
            // Possibly spawn slugs
            world.possiblySpawnEnemies();
        }

        // There can only be a maximum of two slugs
        assertTrue(world.getEnemies().size() <= 2);

    }

    /**
     * Tests that the Doggie changes the selling price of Doggie Coins
     */
    @Test
    public void testDoggieFluctuatingPrice() {

        LoopManiaWorld world = createBasicWorld();

        for (int tick = 0; tick < 9999; tick++) {
            world.randomiseDoggieSellPrice();
            int price = world.getDoggieSellPriceProp().get();
            assertTrue(price <= 200 && price >= 100);
        }

        Doggie doggie = null;
        while (doggie == null) {
            // Attempt to spawn a Doggie
            doggie = world.spawnDoggie();
        }

        for (int tick = 0; tick < 9999; tick++) {
            world.randomiseDoggieSellPrice();
            int price = world.getDoggieSellPriceProp().get();
            // Price should fluctuate even more
            assertTrue(price <= 350 && price >= -50);
        }
        
    }

    @Test
    public void testDoggieAttack() {
        
        LoopManiaWorld world = createBasicWorld();

        // Spawn in a doggie
        Doggie doggie = null;
        while (doggie == null) {
            // Attempt to spawn a Doggie
            doggie = world.spawnDoggie();
        }

        for (int tick = 0; tick < 999; tick++) {
            BasicMove move = doggie.attack(System.currentTimeMillis());
            assertTrue(move instanceof StunHit || move instanceof DoggieHit);
        }
        
    }

    @Test
    public void testElanBoostingDoggieSellPrice() {

        LoopManiaWorld world = createBasicWorld();

        // Normal prices
        for (int tick = 0; tick < 9999; tick++) {
            world.randomiseDoggieSellPrice();
            int price = world.getDoggieSellPriceProp().get();
            assertTrue(price <= 200 && price >= 100);
        }

        world.spawnElanMuske();
        int currentPrice = world.getDoggieSellPriceProp().get();
        int previousPrice = currentPrice;

        while (currentPrice == previousPrice) {
            // Keep randomsing until the price is different
            // as the world has a chance of retaining its previous price
            world.randomiseDoggieSellPrice();
            currentPrice = world.getDoggieSellPriceProp().get();
        }

        for (int tick = 0; tick < 9999; tick++) {
            world.randomiseDoggieSellPrice();
            int price = world.getDoggieSellPriceProp().get();
            // Elan's presence should make the price above 1000
            assertTrue(price >= 1000);
        }
        
    }

    /**
     * Testing that slaying Elan means the price will plummet
     */
    @Test
    public void testElanAnchoringDoggieSellPrice() {

        LoopManiaWorld world = createBasicWorld();

        // Normal prices
        for (int tick = 0; tick < 9999; tick++) {
            world.randomiseDoggieSellPrice();
            int price = world.getDoggieSellPriceProp().get();
            assertTrue(price <= 200 && price >= 100);
        }

        // Simulates that Elan has been slain
        world.setCharacterElanSlayState(true);

        for (int tick = 0; tick < 9999; tick++) {
            world.randomiseDoggieSellPrice();
            int price = world.getDoggieSellPriceProp().get();
            // Elan's death should make the price go down low below 50
            assertTrue(price <= 50);
        }
        
    }

    @Test
    public void testElanAttack() {
        
        LoopManiaWorld world = createBasicWorld();

        // Spawn in Elan
        ElanMuske elan = null;

        elan = world.spawnElanMuske();

        for (int tick = 0; tick < 999; tick++) {
            BasicMove move = elan.attack(System.currentTimeMillis());
            assertTrue(move instanceof HealHit || move instanceof CryptoHit);
        }
        
    }

    @Test
    public void testThiefAttackAndSpawning() {
        LoopManiaWorld world = createBasicWorld();
        Thief thief = null;
        // Changes gold to meet spawning requirements
        world.changeCharacterGold(95);
        int startingGold = world.getCharacterGoldProp().get();
        while (thief == null) {
            thief = world.spawnThief();
        }

        // Gold should be halved when the thief spawns
        assertTrue(world.getCharacterGoldProp().get() != startingGold);

        // Checks that the palyer's inventory is not full
        assertFalse(world.isInventoryFull());

        while (!world.isInventoryFull()) {
            // Fills the player's inventory
            world.addUnequippedPotion();
        }

        // Checks that the player's inventory is full
        assertTrue(world.isInventoryFull());

        int healthBeforeFight = world.getCharacterHealthProp().get();

        try {
            world.runBattles(-1);
        } catch (CharacterIsDead exception) {
            // Fails if character dies since the thief can not hurt the player
            assertTrue(world.getCharacterHealthProp().get() != 0);
        }

        // Checks that the player's inventory should not be full after a fight
        // with the thief
        assertFalse(world.isInventoryFull());

        // Player shoud not have lost any health
        assertTrue(world.getCharacterHealthProp().get() == healthBeforeFight);

        // With the thief defeated, the gold should be returned.
        assertTrue(world.getCharacterGoldProp().get() == startingGold);
        
    }

    /**
     * Creates a basic world that only has 3 tile long path
     * Do not use if you need the character as well
     * @return
     */
    private LoopManiaWorld createBasicWorld() {
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
        return world;
    }

}