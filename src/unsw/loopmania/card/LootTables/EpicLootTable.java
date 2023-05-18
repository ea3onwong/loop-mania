package unsw.loopmania.card.LootTables;

import java.util.Random;

import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.*;

public class EpicLootTable implements CardLootTable {

    @Override
    public StaticEntity generateItem() {

        
        int dropRate = (new Random(System.currentTimeMillis())).nextInt(100);

        if (dropRate < 5) {
            // 5% Sword
            return new Sword(null, null);
        } else if (dropRate < 20) {
            // 15% Stake
            return new Stake(null, null);
        } else if (dropRate < 40) {
            // 20% Staff
            return new Staff(null, null);
        } else if (dropRate < 45) {
            // 5% Helmet
            return new Helmet(null, null);
        } else if (dropRate < 60) {
            // 15% Shield 
            return new Shield(null, null);
        } else if (dropRate < 80) {
            // 20% Armour
            return new Armour(null, null);
        } else {
            // 20% Potion
            return new Potion(null, null);
        }
    }

}

