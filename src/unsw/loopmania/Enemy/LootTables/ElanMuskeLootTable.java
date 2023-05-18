package unsw.loopmania.Enemy.LootTables;

import java.util.Random;

import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.*;
import unsw.loopmania.card.*;

public class ElanMuskeLootTable implements BasicLootTable {

    @Override
    public StaticEntity generateItem() {
        int dropRate = (new Random(System.currentTimeMillis())).nextInt(100);

        if (dropRate < 20) {
            // 20% The One Ring
            return new TheOneRing(null, null);
        } else if (dropRate < 60) {
            // 40% Anduril
            return new Anduril(null, null);
        } else {
            // 40% Tree Stump
            return new TreeStump(null, null);
        }
    }

    @Override
    public StaticEntity generateBuilding() {

        int dropRate = (new Random(System.currentTimeMillis())).nextInt(100);

        if (dropRate < 15) {
            // 15% Vampire Castle
            return new VampireCastleCard(null, null);
        } else if (dropRate < 35) {
            // 20% Campfire
            return new CampfireCard(null, null);
        } else if (dropRate < 60) {
            // 25% Field Kitchen
            return new FieldKitchenCard(null, null);
        } else {
            // 40% Holy Spring
            return new HolySpringCard(null, null);
        }
    }
    
}

