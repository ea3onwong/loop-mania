package unsw.loopmania.card.LootTables;

import java.util.Random;

import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.*;

public class LegendLootTable implements CardLootTable {

    @Override
    public StaticEntity generateItem() {

        
        int dropRate = (new Random(System.currentTimeMillis())).nextInt(100);

        if (dropRate < 50) {
            // 50% nothing
            return null;
        } else if (dropRate < 60) {
            // 10% The One Ring
            return new TheOneRing(null, null);
        } else if (dropRate < 80) {
            // 20% Anduril
            return new Anduril(null, null);
        } else {
            // 20% Tree Stump
            return new TreeStump(null, null);
        }
    }
}