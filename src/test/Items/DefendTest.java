package test.Items;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

public class DefendTest {

    @Test 
    public void testReceiveDamage() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy slug = new Slug(position);

        character.receiveDamage(slug, slug.attack(1).getDamage());
        assertEquals(100 - slug.attack(1).getDamage(), character.getIntHP());
    }

    @Test
    public void testHelmet() {
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
        world.addBackendHelmet(1, 0);

        character.receiveDamage(slug, slug.attack(0).getDamage());
        character.receiveDamage(slug, zombie.attack(0).getDamage());

        assertEquals(100, character.getIntHP()); 

        world.addBackendSword(0, 0);

        int initialVampireHP = vampire.getHealth();
        int initialCharacterHP = character.getIntHP();
        int bsaeAttack = character.getIntBaseATK();

        character.attackEnemy(vampire);
        character.receiveDamage(vampire, 20);
        
        // reduce 20% damage to enemeies if the player equippped helemt.
        int damage = (bsaeAttack + 10) * 4 / 5;
        int expectedVampireHP = initialVampireHP - damage;
        if (expectedVampireHP < 0) {expectedVampireHP = 0;}

        assertEquals(expectedVampireHP, vampire.getHealth());

        // reduce 5 damage from enemy attack (20 - 5)
        int expectedCharacterHP = initialCharacterHP - (20-5);
        assertEquals(expectedCharacterHP, character.getIntHP());
    }

    @Test
    public void testArmour() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy vampire = new Vampire(position);

        world.setCharacter(character);

        world.addBackendArmour(1, 0);

        int enemyAttack = vampire.attack(System.currentTimeMillis()).getDamage();
        character.receiveDamage(vampire, enemyAttack);
        assertEquals(100 - enemyAttack/2, character.getIntHP());
    }

    @Test
    public void testShield() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy vampire = new Vampire(position);

        world.setCharacter(character);
        world.addBackendShield(2, 0);

        ArrayList<Integer> damages = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int damage = character.receiveDamage(vampire, 20); // always do critical attack 
            damages.add(damage);
        }
        // critical vampire attacks have a 60% lower chance of occurring (convert it to normal attack)
        // must include either normal attack or critical attack
        assertTrue(damages.contains(10)); 
        assertTrue(damages.contains(20));
    }

    @Test
    public void testTreeStump() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy slug = new Slug(position);
        BasicEnemy zombie = new Zombie(position);
        BasicEnemy vampire = new Vampire(position);
        Doggie doggie = new Doggie(position);
        world.setGamemodeState(new GamemodeConfusing());
        world.setCharacter(character);
        world.addBackendTreeStump(2, 0);

        character.receiveDamage(slug, slug.attack(0).getDamage());
        assertEquals(100, character.getIntHP()); 
        character.receiveDamage(zombie, zombie.attack(0).getDamage());
        assertEquals(100, character.getIntHP()); 
        character.receiveDamage(vampire, vampire.attack(2).getDamage()); // 0 and 1 crits
        assertEquals(95, character.getIntHP());
        character.receiveDamage(doggie, doggie.attack(2).getDamage()); // 0 and 1 crits
        // currHP 95, doggie deals 30, TreeStump reduces it by 10 if its a boss, so 95 - (30 - 10) = 75
        assertEquals(75, character.getIntHP()); 
    }

    @Test
    public void testTreeStumpANDHelmet() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy slug = new Slug(position);
        BasicEnemy zombie = new Zombie(position);
        BasicEnemy vampire = new Vampire(position);
        Doggie doggie = new Doggie(position);
        world.setGamemodeState(new GamemodeConfusing());
        world.setCharacter(character);
        world.addBackendHelmet(1, 0);
        world.addBackendTreeStump(2, 0);

        character.receiveDamage(slug, slug.attack(0).getDamage());
        assertEquals(100, character.getIntHP()); 

        character.receiveDamage(zombie, zombie.attack(0).getDamage());
        assertEquals(100, character.getIntHP()); 

        character.receiveDamage(vampire, vampire.attack(2).getDamage()); // 0 and 1 crits
        assertEquals(100, character.getIntHP());

        character.receiveDamage(doggie, doggie.attack(2).getDamage()); // 0 and 1 crits
        // currHP 100, doggie deals 30, TreeStump reduces it by 10 if its a boss and Helmet by another 5, so 100 - (30 - 10 - 5) = 85
        assertEquals(85, character.getIntHP()); 

    }

    @Test
    public void testTreeStumpANDArmour() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy slug = new Slug(position);
        BasicEnemy zombie = new Zombie(position);
        BasicEnemy vampire = new Vampire(position);
        Doggie doggie = new Doggie(position);
        world.setGamemodeState(new GamemodeConfusing());
        world.setCharacter(character);
        world.addBackendArmour(1, 0);
        world.addBackendTreeStump(2, 0);

        // armour is accounted for first
        character.receiveDamage(slug, slug.attack(0).getDamage());
        assertEquals(100, character.getIntHP()); 

        character.receiveDamage(zombie, zombie.attack(0).getDamage());
        assertEquals(100, character.getIntHP()); 

        character.receiveDamage(vampire, vampire.attack(2).getDamage()); // 0 and 1 crits
        assertEquals(100, character.getIntHP());

        character.receiveDamage(doggie, doggie.attack(2).getDamage()); // 0 and 1 crits
        // currHP 100, doggie deals 30, Armour reduces it to 15 then TreeStump reduces it by 10 if its a boss, so 100 - ((30/2) - 10) = 95
        assertEquals(95, character.getIntHP()); 
    }
}


