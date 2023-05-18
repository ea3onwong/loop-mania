package unsw.loopmania.Enemy.LootTables;

import java.util.Random;

import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.*;
import unsw.loopmania.card.*;

public class ZombieLootTable implements BasicLootTable {

    @Override
    public StaticEntity generateItem() {

        int dropRate = (new Random(System.currentTimeMillis())).nextInt(1000);

        if (dropRate < 100) { 
            // 10% Nothing
            return null;
        } else if (dropRate < 250) {
            // 15% Sword
            return new Sword(null, null);
        } else if (dropRate < 350) {
            // 10% Stake
            return new Stake(null, null);
        } else if (dropRate < 450) {
            // 10% Staff
            return new Staff(null, null);
        } else if (dropRate < 600) {
            // 15% Helmet
            return new Helmet(null, null);
        } else if (dropRate < 700) {
            // 10% Shield
            return new Shield(null, null);
        } else if (dropRate < 800) {
            // 10% Armour
            return new Armour(null, null);
        } else if (dropRate < 900) {
            // 10% Potion
            return new Potion(null, null);
        } else if (dropRate < 950) {
            // 5% The One Ring
            return new TheOneRing(null, null);
        } else if (dropRate < 975) {
            // 2.5% Anduril
            return new Anduril(null, null);
        } else {
            return new TreeStump(null, null);
        }
    }

    @Override
    public StaticEntity generateBuilding() {

        int dropRate = (new Random(System.currentTimeMillis())).nextInt(100);

        if (dropRate < 10) {
            // 10% Trap
            return new TrapCard(null, null);
        } else if (dropRate < 20) {
            // 10% Tower
            return new TowerCard(null, null);
        } else if (dropRate < 30) {
            // 10% Zombie Pit
            return new ZombiePitCard(null, null);
        } else if (dropRate < 45){
            // 15% Village
            return new VillageCard(null, null);
        } else if (dropRate < 60) {
            // 15% Barracks
            return new BarracksCard(null, null);
        } else if (dropRate < 70) {
            // 10% Alchemist
            return new AlchemistTentCard(null, null);
        } else if (dropRate < 90) {
            // 20% Survivors Camp
            return new SurvivorsTentsCard(null, null);
        } else {
            // 10% Vampire Castle
            return new VampireCastleCard(null, null);
        }

    }
    
}

