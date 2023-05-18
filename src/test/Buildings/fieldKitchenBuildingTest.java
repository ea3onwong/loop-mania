package test.Buildings;

import org.junit.Test;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.building.FieldKitchenBuilding;
import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class fieldKitchenBuildingTest {
    @Test
    public void fieldKitchenTest() {
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        

        // add a Field Kitchen building into world
        FieldKitchenBuilding fieldKitchenBuilding = new FieldKitchenBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(fieldKitchenBuilding);

        // get the return value from checkCharacterStepOnFieldKitchen, which is the num of ration could get
        int numRation = world.checkCharacterStepOnFieldKitchen();

        // character can get 1 ration
        assertEquals(numRation, 1);

        // if field kitchen is next to village, then character can get 2 rations
        VillageBuilding villageBuilding = new VillageBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        world.addBuildingEntity(villageBuilding);
        numRation = world.checkCharacterStepOnFieldKitchen();
        assertEquals(numRation, 2);

        // if campfire is next to a field kitchen, then character can be healed by 5 for each step he walk in the radius of campfire
        CampfireBuilding campfireBuilding = new CampfireBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0));
        world.addBuildingEntity(campfireBuilding);
        // set a damage on the character
        assertEquals(character.getIntHP(), 100);
        character.changeHealth(-20);
        assertEquals(character.getIntHP(), 80);
        // run Buildings
        world.runBuildings();
        assertEquals(character.getIntHP(), 85);
    }
}
