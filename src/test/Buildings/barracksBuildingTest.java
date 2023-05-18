package test.Buildings;

import org.junit.Test;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.building.BarracksBuilding;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Enemy.Zombie;


public class barracksBuildingTest {
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

        // set new barracks into the world
        BarracksBuilding barracksBuilding = new BarracksBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        
        // now the character has 0 allied soldier
        assertEquals(character.getIntAllies(), 0);

        // after entering the barrack, the character will now have 1 allied soldier
        barracksBuilding.performAbility(character);
        assertEquals(character.getIntAllies(), 1);

        // after entering the barrack again, the character will now have 2 allied soldiers
        barracksBuilding.performAbility(character);
        assertEquals(character.getIntAllies(), 2);

        // after entering the barrack again, the character will now have 3 allied soldiers which reach the max
        barracksBuilding.performAbility(character);
        assertEquals(character.getIntAllies(), 3);

        // after entering the barrack again, the character will now have 3 allied soldiers which reach the max
        barracksBuilding.performAbility(character);
        assertEquals(character.getIntAllies(), 3);

        // if the character has allied soldiers and has a battle with a zombie and receive a critical damage against soldier, 
        // then an allied soldier will turn into zombie, and number of allied soldiers decreased by 1
        Zombie zombie = new Zombie(position);
        if (!world.individualFight(zombie, System.currentTimeMillis(), null)){
            assertEquals(character.getIntAllies(), 2);
        }
    }
}
