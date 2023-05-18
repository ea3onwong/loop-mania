package test.Items;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

import org.javatuples.Pair;

public class ItemReplacementTest {
    @Test
    public void testItemReplacement() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<Pair<Integer, Integer>>();
        Pair<Integer, Integer> pair = Pair.with(0, 0);
        orderedPath.add(pair);

        LoopManiaWorld world = new LoopManiaWorld(3, 3, orderedPath);
        PathPosition position = new PathPosition(0, orderedPath);
        Character character = new Character(position);
        int starttingGold = character.getIntGold();
        int starttingExp = character.getIntExp();
        
        world.setCharacter(character);

        for (int i = 0; i <= 15; i++) {
            world.addUnequippedSword();
        }

        assertEquals(starttingGold, character.getIntGold());
        assertEquals(starttingExp, character.getIntExp());

        world.addUnequippedSword();

        assertEquals(starttingGold + 5, character.getIntGold());
        assertEquals(starttingExp + 5, character.getIntExp());

        world.addUnequippedSword();

        assertEquals(starttingGold + 10, character.getIntGold());
        assertEquals(starttingExp + 10, character.getIntExp());
    }
}
