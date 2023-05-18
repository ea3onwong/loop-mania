package unsw.loopmania.card.LootTables;

import java.util.Random;

import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.*;

public class NormalLootTable implements CardLootTable {

    @Override
    public StaticEntity generateItem() {


        int dropRate = (new Random(System.currentTimeMillis())).nextInt(100);

        if (dropRate < 40) {
            // 40% Sword
            return new Sword(null, null);
        } else if (dropRate < 80) {
            // 40% Helmet
            return new Helmet(null, null);
        } else {
            // 20% potion
            return new Potion(null, null);
        } 
        
    }
    
}

