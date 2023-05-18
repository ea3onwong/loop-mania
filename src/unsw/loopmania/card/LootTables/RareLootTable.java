package unsw.loopmania.card.LootTables;

import java.util.Random;

import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.*;

public class RareLootTable implements CardLootTable {

    @Override
    public StaticEntity generateItem() {


        int dropRate = (new Random(System.currentTimeMillis())).nextInt(100);

        if (dropRate < 20) {
            // 20% Sword
            return new Sword(null, null);
        } else if (dropRate < 30) {
            // 10% Stake
            return new Stake(null, null);
        } else if (dropRate < 40) {
            // 10% Staff
            return new Staff(null, null);
        } else if (dropRate < 60) {
            // 20% Helmet
            return new Helmet(null, null);
        } else if (dropRate < 70) {
            // 10% Shield
            return new Shield(null, null);
        } else if (dropRate < 80) {
            // 10% Armour
            return new Armour(null, null);
        } else {
            // 20% Potion
            return new Potion(null, null);
        }
    }

    
}

