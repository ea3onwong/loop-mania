package test.Buildings;

import org.junit.Test;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class campfireBuildingTest {
    @Test
    public void campfireTest() {
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        // set a new campfire into the world, and the character with no weapons equipped deals 2 damage to all enemies
        CampfireBuilding campfireBuilding = new CampfireBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertEquals(character.getIntAttack(), 2); 

        // after the character enter the radius of campfire, the damge of character will be doubled
        campfireBuilding.performAbility(character);
        assertEquals(character.getIntAttack(), 4); 

        // if the character enter the radius of two campfire, the damge of character will be doubled again
        campfireBuilding.performAbility(character);
        assertEquals(character.getIntAttack(), 8); 
    }
}
