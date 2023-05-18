package test.Buildings;

import org.junit.Test;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Enemy.Slug;

public class villageBuildingTest {
    @Test
    public void villageTest() {
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        // check the HP of character at the beginning which is 100
        assertEquals(character.getIntHP(), 100);

        // check the HP of character after receiving 10 damage from the slug, now the HP of character is 90
        Slug slug = new Slug(position);
        character.receiveDamage(slug, 10);
        assertEquals(character.getIntHP(), 90);

        // add a village building into world
        VillageBuilding villageBuilding = new VillageBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(villageBuilding);

        // when character walk through the village, the character will get healed by 20 HP, now the HP of character is 100
        villageBuilding.performAbility(character);
        assertEquals(character.getIntHP(), 100);

        // if the character pass through a village again, the HP of character will still be 100 which is the max HP
        villageBuilding.performAbility(character);
        assertEquals(character.getIntHP(), 100);
        
        // then after receving 40 damage from slug, and the character walk through the village, 
        // the character will get healed by 20 HP, now the HP of character is 80
        character.receiveDamage(slug, 40);
        villageBuilding.performAbility(character);
        assertEquals(character.getIntHP(), 80);
    }

    
}
