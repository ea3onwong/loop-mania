package test.Buildings;

import org.junit.Test;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.building.HeroCastleBuilding;
import unsw.loopmania.building.SurvivorsTentsBuilding;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Enemy.Slug;

public class survivorsTentsBuildingTest {
    @Test
    public void survivorsTentsTest() {
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        // set Hero Castle Building to the world
        HeroCastleBuilding heroCastleBuilding = new HeroCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(heroCastleBuilding);
        
        // set new Survivors' Tents into the world
        SurvivorsTentsBuilding survivorsTentsBuilding = new SurvivorsTentsBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(survivorsTentsBuilding);
        assertEquals(world.getBuildingEntities().size(), 2);

        // now the character has 100 HP, and 100 MAX HP
        assertEquals(character.getIntMaxHP(), 100);
        assertEquals(character.getIntHP(), 100);

        // after the character passing through the Survivors' Tents, the MAX HP will increased by 5
        world.runBuildings();
        assertEquals(character.getIntMaxHP(), 105);
        assertEquals(character.getIntHP(), 105);
        // the survivors' tents will disappear in the next loop
        assertEquals(world.getBuildingEntities().size(), 1);

        SurvivorsTentsBuilding survivorsTentsBuilding2 = new SurvivorsTentsBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(survivorsTentsBuilding2);
        // after the character passing through the Survivors' Tents, the MAX HP will increased by 5
        world.runBuildings(); 
        assertEquals(character.getIntMaxHP(), 110);
        assertEquals(character.getIntHP(), 110);
        assertEquals(world.getBuildingEntities().size(), 1);

        // character receive 10 damge from slug
        Slug slug = new Slug(position);
        character.receiveDamage(slug, 10);
        assertEquals(character.getIntMaxHP(), 110);
        assertEquals(character.getIntHP(), 100);

        SurvivorsTentsBuilding survivorsTentsBuilding3 = new SurvivorsTentsBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(survivorsTentsBuilding3);
        // after the character passing through the Survivors' Tents, the MAX HP will increased by 5
        world.runBuildings();
        assertEquals(character.getIntMaxHP(), 115);
        assertEquals(character.getIntHP(), 105);
        assertEquals(world.getBuildingEntities().size(), 1);
    }
}
