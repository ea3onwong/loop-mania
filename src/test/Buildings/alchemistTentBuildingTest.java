package test.Buildings;

import org.junit.Test;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.building.AlchemistTentBuilding;
import unsw.loopmania.building.HeroCastleBuilding;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class alchemistTentBuildingTest {
    @Test
    public void alchemistTentTest() {
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

        // set new Alchemist's Tent into the world
        AlchemistTentBuilding alchemistTentBuilding = new AlchemistTentBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(alchemistTentBuilding);
        assertEquals(world.getBuildingEntities().size(), 2);

        // now the character has 2 base attack, and 2 total attack
        assertEquals(character.getIntBaseATK(), 2);
        assertEquals(character.getIntAttack(), 2);

        // after the character passing through the alchemist's tent, the base attack will increased by 5
        world.runBuildings();
        assertEquals(character.getIntBaseATK(), 7);
        assertEquals(character.getIntAttack(), 7);
        // the alchemist's tent will disappear in the next loop
        assertEquals(world.getBuildingEntities().size(), 1);

        AlchemistTentBuilding alchemistTentBuilding2 = new AlchemistTentBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(alchemistTentBuilding2);
        // after the character passing through the alchemist's tent, the base attack will increased by 5
        world.runBuildings();
        assertEquals(character.getIntBaseATK(), 12);
        assertEquals(character.getIntAttack(), 12);
        // the alchemist's tent will disappear in the next loop
        assertEquals(world.getBuildingEntities().size(), 1);

    }
    
}
