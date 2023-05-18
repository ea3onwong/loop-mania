package test.Buildings;

import org.junit.Test;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.building.TrapBuilding;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Enemy.Slug;
import unsw.loopmania.Enemy.Vampire;
import unsw.loopmania.Enemy.Zombie;

public class trapBuildingTest {
    @Test
    public void trapTest() {
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        // check if there are any enemies in the world, now is 0
        assertEquals(world.getEnemies().size(), 0);
        
        // add a slug to the world which has 10 HP, add a trap to the world
        // NOTE: in this test, the trap will not destory after enemies stepping on it, it will normally destory in the game
        Slug slug = new Slug(position);
        assertEquals(slug.getHealth(), 10);
        TrapBuilding trapBuilding = new TrapBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));

        // after the slug step on the trap, it will get damage by 20, now the slug has 0 HP which means it's dead
        trapBuilding.performAbility(slug);
        assertEquals(slug.getHealth(), 0);

        // add a zombie to the world which has 25 HP
        Zombie zombie = new Zombie(position);
        assertEquals(zombie.getHealth(), 25);

        // after the zombie step on the trap, it will get damage by 20, now the zombie has 5 HP
        trapBuilding.performAbility(zombie);
        assertEquals(zombie.getHealth(), 5);
        // after the zombie step on the trap again, it will get damage by 20, now the zombie has 0 HP which means it's dead
        trapBuilding.performAbility(zombie);
        assertEquals(zombie.getHealth(), 0);

        // add a vampire to the world which has 25 HP
        Vampire vampire = new Vampire(position);
        assertEquals(vampire.getHealth(), 75);

        // after the vampire step on the trap, it will get damage by 20, now the vampire has 55 HP
        trapBuilding.performAbility(vampire);
        assertEquals(vampire.getHealth(), 55);
        // after the vampire step on the trap again, it will get damage by 20, now the vampire has 35 HP
        trapBuilding.performAbility(vampire);
        assertEquals(vampire.getHealth(), 35);
        // after the vampire step on the trap again, it will get damage by 20, now the vampire has 15 HP
        trapBuilding.performAbility(vampire);
        assertEquals(vampire.getHealth(), 15);
        // after the vampire step on the trap again, it will get damage by 20, now the vampire has 0 HP which means it's dead
        trapBuilding.performAbility(vampire);
        assertEquals(vampire.getHealth(), 0);
    }
}
