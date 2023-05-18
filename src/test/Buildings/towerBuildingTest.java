package test.Buildings;

import org.junit.Test;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class towerBuildingTest {
    @Test
    public void towerTest() {
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        // set a new tower into the world, and the character with no weapons equipped deals 2 damage to all enemies
        TowerBuilding towerBuilding = new TowerBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        assertEquals(character.getIntAttack(), 2); 

        // after the character enter the radius of tower while having a battle, the tower will add bouns damge to character by 20
        towerBuilding.performAbility(character);
        assertEquals(character.getIntAttack(), 22); 

        // if the character enter the radius of two towers while having a battle, the tower will add bouns damge to character by 40 
        towerBuilding.performAbility(character);
        assertEquals(character.getIntAttack(), 42); 
    }
}