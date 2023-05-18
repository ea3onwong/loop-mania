package test.Cards;

import org.junit.Test;
import org.javatuples.Pair;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class barracksCardTest {
    @Test
    public void barracksTest() {
        // Creating the world and its character
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0,0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        world.setCharacter(character);

        //Check the entity of cards and building, should be 0 for both at the beginning
        assertEquals(world.getCardEntities().size(), 0);
        assertEquals(world.getBuildingEntities().size(), 0);

        //Load one barracks card and check again the entity for both, now card entity should be 1 and building entity should be 0
        world.loadBarracksCard();
        assertEquals(world.getCardEntities().size(), 1);
        assertEquals(world.getBuildingEntities().size(), 0);

        //convert card into building and check again the entity for both, now card entity should be 0 and building entity should be 1
        world.convertBarracksCardToBuildingByCoordinates(0,0,0,0);
        assertEquals(world.getCardEntities().size(), 0);
        assertEquals(world.getBuildingEntities().size(), 1);
    }
}
