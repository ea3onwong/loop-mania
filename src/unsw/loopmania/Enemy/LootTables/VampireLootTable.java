package unsw.loopmania.Enemy.LootTables;

import java.util.Random;

import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.*;
import unsw.loopmania.card.*;

public class VampireLootTable implements BasicLootTable {

    @Override
    public StaticEntity generateItem() {
        int dropRate = (new Random(System.currentTimeMillis())).nextInt(100);

        if (dropRate < 5) {
            // 5% Sword
            return new Sword(null, null);
        } else if (dropRate < 15) {
            // 10% Stake
            return new Stake(null, null);
        } else if (dropRate < 30) {
            // 15% Staff
            return new Staff(null, null);
        } else if (dropRate < 35) {
            // 5% Helmet
            return new Helmet(null, null);
        } else if (dropRate < 45) {
            // 10% Shield 
            return new Shield(null, null);
        } else if (dropRate < 60) {
            // 15% Armour
            return new Armour(null, null);
        } else if (dropRate < 70) {
            // 10% Potion
            return new Potion(null, null);
        } else if (dropRate < 80) {
            // 10% The One Ring
            return new TheOneRing(null, null);
        } else if (dropRate < 90) {
            // 10% Anduril
            return new Anduril(null, null);
        } else {
            // 10% Tree Stump
            return new TreeStump(null, null);
        }
    }

    @Override
    public StaticEntity generateBuilding() {

        int dropRate = (new Random(System.currentTimeMillis())).nextInt(1000);

        if (dropRate < 100){
            // 10% Village
            return new VillageCard(null, null);
        } else if (dropRate < 200) {
            // 10% Barracks
            return new BarracksCard(null, null);
        } else if (dropRate < 350) {
            // 15% Alchemist
            // return new AlchemistCard(null, null);
            return null;
        } else if (dropRate < 500) {
            // 15% Survivors Camp
            // return new SurvivorsCamp(null, null);
            return null;
        } else if (dropRate < 575) {
            // 7.5% Vampire Castle
            return new VampireCastleCard(null, null);
        } else if (dropRate < 775) {
            // 20% Campfire
            return new CampfireCard(null, null);
        } else if (dropRate < 975) {
            // 20% Field Kitchen
            return new FieldKitchenCard(null, null);
        } else {
            // 2.5% Holy Spring
            return new HolySpringCard(null, null);
        }
    }
    
}

