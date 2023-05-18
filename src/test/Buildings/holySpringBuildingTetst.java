package test.Buildings;

import org.junit.Test;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.building.HolySpringBuilding;
import unsw.loopmania.Character;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Enemy.Slug;

public class holySpringBuildingTetst {
    @Test
    public void holySpringTest() {
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

        // add a Holy Spring building into world
        HolySpringBuilding holySpringBuilding = new HolySpringBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(holySpringBuilding);
        assertEquals(world.getBuildingEntities().size(), 1);

        // when character walk through the Holy Spring, the character will get healed by FULL HP, now the HP of character is 100
        world.runBuildings();
        assertEquals(character.getIntHP(), 100);
        // then the holy spring will disappear after used
        assertEquals(world.getBuildingEntities().size(), 0);

        // if the character pass through a Holy Spring again, the HP of character will still be 100 which is the max HP
        HolySpringBuilding holySpringBuilding2 = new HolySpringBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(holySpringBuilding2);
        assertEquals(world.getBuildingEntities().size(), 1);
        world.runBuildings();
        assertEquals(character.getIntHP(), 100);
        // the holy spring won't disappear as the character is FULL HP
        assertEquals(world.getBuildingEntities().size(), 1);
        
        // then after receving 99 damage from slug, and the character walk through the Holy Spring, 
        // the character will get healed by Full HP, now the HP of character is 100
        character.receiveDamage(slug, 99);
        world.runBuildings();
        assertEquals(character.getIntHP(), 100);
        // then the holy spring will disappear after used
        assertEquals(world.getBuildingEntities().size(), 0);

        // change the max health of character to 120 HP
        character.changeMaxHealth(20);
        assertEquals(character.getIntHP(), 120);

        // After receving 99 damge from slug, and walk through a Holy Spring, then character will get healthed by Full HP
        character.receiveDamage(slug, 99);
        HolySpringBuilding holySpringBuilding3 = new HolySpringBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.addBuildingEntity(holySpringBuilding3);
        assertEquals(world.getBuildingEntities().size(), 1);
        world.runBuildings();
        assertEquals(character.getIntHP(), 120);
        // then the holy spring will disappear after used
        assertEquals(world.getBuildingEntities().size(), 0);
    }
}
