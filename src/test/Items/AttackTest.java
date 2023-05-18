package test.Items;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Boss.Doggie;
import unsw.loopmania.Enemy.BasicEnemy;
import unsw.loopmania.Enemy.Slug;
import unsw.loopmania.Enemy.Vampire;
import unsw.loopmania.Enemy.Zombie;
import unsw.loopmania.Gamemodes.GamemodeConfusing;

import org.javatuples.Pair;


public class AttackTest {
    @Test 
    public void testSimpleAttack() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy slug = new Slug(position);
        BasicEnemy zombie = new Zombie(position);

        int slugHP = slug.getHealth();
        int zombieHP = zombie.getHealth();

        character.attackEnemy(slug);
        assertEquals(slugHP-2, slug.getHealth());

        character.attackEnemy(zombie);
        assertEquals(zombieHP-2, zombie.getHealth());
    }

    @Test 
    public void testSword() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy slug = new Slug(position);
        BasicEnemy zombie = new Zombie(position);
        
        int slugHP = slug.getHealth();
        int zombieHP = zombie.getHealth();
        int baseAttack = character.getIntAttack();

        world.setCharacter(character);
        world.addBackendSword(0, 0);
        character.attackEnemy(slug);
        character.attackEnemy(zombie);

        int expectedSlugHP = slugHP - baseAttack - 10;
        if (expectedSlugHP < 0) {expectedSlugHP = 0;}
        assertEquals(expectedSlugHP, slug.getHealth());

        int expectedZombieHP = zombieHP - baseAttack - 10;
        if (expectedZombieHP < 0) {expectedZombieHP = 0;}
        assertEquals(expectedZombieHP, zombie.getHealth());
    }

    @Test 
    public void testStake() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy slug = new Slug(position);
        BasicEnemy zombie = new Zombie(position);
        BasicEnemy vampire = new Vampire(position); 

        world.setCharacter(character);
        world.addBackendStake(0, 0);
        int slugHP = slug.getHealth();
        int zombieHP = zombie.getHealth();
        int vampireHP = vampire.getHealth();
        int baseAttack = character.getIntBaseATK();

        character.attackEnemy(slug);
        int expectedSlugHP = slugHP - baseAttack - 5;
        if (expectedSlugHP < 0) {expectedSlugHP = 0;}
        assertEquals(expectedSlugHP, slug.getHealth());

        character.attackEnemy(zombie);
        int expectedZombieHP = zombieHP - baseAttack - 5;
        if (expectedZombieHP < 0) {expectedZombieHP = 0;}
        assertEquals(expectedZombieHP, zombie.getHealth());

        character.attackEnemy(vampire);
        int expectedVampireHP = vampireHP - baseAttack - 20;
        if (expectedVampireHP < 0) {expectedVampireHP = 0;}
        assertEquals(expectedVampireHP, vampire.getHealth());
    } 
    
    @Test
    public void testStaff1() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        world.setCharacter(character);
        world.addBackendStaff(0, 0);

        boolean slugIsTranced = false;
        boolean zombieIsTranced = false;

        for (int i = 0; i < 50; i++) {
            BasicEnemy slug = new Slug(position);
            BasicEnemy zombie = new Zombie(position);
            character.attackEnemy(slug);
            character.attackEnemy(zombie);
            if (slug.isTranced()) {
                slugIsTranced = true;
            }
            if (zombie.isTranced()) {
                zombieIsTranced = true;
            }
        }

        // the slug and should at least be tranced once.
        assertTrue(slugIsTranced); 
        assertTrue(zombieIsTranced);
    }

    @Test 
    public void testStaff2() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        world.setCharacter(character);
        world.addBackendStaff(0, 0);
        
        BasicEnemy vampire = new Vampire(position);
        vampire.setTranceStatus(true);

        int baseAttack = character.getIntBaseATK();
        int attackDamage = character.attackEnemy(vampire);

        assertTrue(attackDamage == baseAttack + 3 + 10 ||  attackDamage == baseAttack + 3 + 20);

        // no damage to the player if the vampire is tranced
        int hpBeforeReceiveDamage = character.getIntHP();
        character.receiveDamage(vampire, vampire.attack(0).getDamage());
        assertEquals(hpBeforeReceiveDamage, character.getIntHP());

        for (int i = 0; i <= 5; i++) {
        character.attackEnemy(vampire);
        character.receiveDamage(vampire, 10);
        }

        // if enemy is tranced more than 5 rounds of fight, it would revert back to normal status.
        assertFalse(vampire.isTranced());
    }
    
    @Test
    public void testAnduril() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        world.setGamemodeState(new GamemodeConfusing());
        world.setCharacter(character);
        world.addBackendAnduril(0, 0);

        BasicEnemy zombie = new Zombie(position);
        BasicEnemy vampire = new Vampire(position); 
        Doggie doggie = new Doggie(position);
        int zombieHP = zombie.getHealth();
        int vampireHP = vampire.getHealth();
        int doggieHP = doggie.getHealth();
        int baseAttack = character.getIntBaseATK();

    
        character.attackEnemy(zombie);
        assertEquals(zombieHP - baseAttack - 15, zombie.getHealth());

        character.attackEnemy(vampire);
        assertEquals(vampireHP - baseAttack - 15, vampire.getHealth());
        
        character.attackEnemy(doggie);
        assertEquals(doggieHP - baseAttack - 45, doggie.getHealth());
    }
}
