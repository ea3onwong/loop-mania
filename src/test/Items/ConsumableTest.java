package test.Items;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Enemy.BasicEnemy;
import unsw.loopmania.Enemy.Slug;
import unsw.loopmania.Enemy.Zombie;
import unsw.loopmania.Items.Potion;

import org.javatuples.Pair;


public class ConsumableTest {
    @Test 
    public void testConsumePotion() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy slug = new Slug(position);
        BasicEnemy zombie = new Zombie(position);
        
        character.receiveDamage(slug, slug.attack(System.currentTimeMillis()).getDamage());
        character.receiveDamage(zombie, zombie.attack(System.currentTimeMillis()).getDamage());
        assertEquals(100 - slug.attack(System.currentTimeMillis()).getDamage() - zombie.attack(System.currentTimeMillis()).getDamage(), character.getIntHP());
        
        world.setCharacter(character);
        world.addUnequippedPotion();
        world.consumePotion();
        assertEquals(character.getIntHP(), 100);
    }

    @Test 
    public void testConsumeRation() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy slug = new Slug(position);
        BasicEnemy zombie = new Zombie(position);
        
        character.receiveDamage(slug, slug.attack(System.currentTimeMillis()).getDamage());
        character.receiveDamage(zombie, zombie.attack(System.currentTimeMillis()).getDamage());
        assertEquals(100 - slug.attack(System.currentTimeMillis()).getDamage() - zombie.attack(System.currentTimeMillis()).getDamage(), character.getIntHP());
        
        world.setCharacter(character);
        world.addUnequippedRation();
        world.consumeRation();
        assertEquals(character.getIntHP(), 100);

        character.changeHealth(-50);
        world.addUnequippedRation();
        world.consumeRation();
        // the ration can heal the character by 30
        assertEquals(character.getIntHP(), 80);
    }

    @Test
    public void testConsumeRing() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        BasicEnemy zombie = new Zombie(position);

        world.setCharacter(character);
        world.addUnequippedTheOneRing();
        
        while (character.getIntHP() != 0) {
            character.receiveDamage(zombie, zombie.attack(System.currentTimeMillis()).getDamage());
        }
        assertEquals(0, character.getIntHP());
        world.consumeRevivable((new Potion(null, null).toString()));
        assertEquals(100, character.getIntHP());
    }
}