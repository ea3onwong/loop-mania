package unsw.loopmania.Enemy.LootTables;

import java.util.Random;

import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.*;
import unsw.loopmania.card.*;

public class SlugLootTable implements BasicLootTable {

    @Override
    public StaticEntity generateItem() {

        int dropRate = (new Random(System.currentTimeMillis())).nextInt(100);

        if (dropRate < 20) {
            // 20% Nothing
            return null;
        } else if (dropRate < 50) {
            // 30% Sword
            return new Sword(null, null);
        } else if (dropRate < 80) {
            // 30% Helmet
            return new Helmet(null, null);
        } else if (dropRate < 95) {
            // 15% potion
            return new Potion(null, null);
        } else if (dropRate < 98) {
            // 3% The One Ring
            return new TheOneRing(null, null);
        } else if (dropRate < 99) {
            // 1% Anduril
            return new Anduril(null, null);
        } else {
            // 1% Tree Stump
            return new TreeStump(null, null);
        }
        
    }

    @Override
    public StaticEntity generateBuilding() {

        int dropRate = (new Random(System.currentTimeMillis())).nextInt(10);

        if (dropRate < 2) {
            // 20% nothing
            return null;
        } else if (dropRate < 4) {
            // 20% Trap
            return new TrapCard(null, null);
        } else if (dropRate < 7) {
            // 20% Zombie Pit
            return new ZombiePitCard(null, null);
        } else {
            // 30% Tower
            return new TowerCard(null, null);
        }
    }
    
}

