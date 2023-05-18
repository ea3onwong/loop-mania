/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.Enemy;

import java.util.Random;

import unsw.loopmania.PathPosition;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.Enemy.EnemyMoves.*;
import unsw.loopmania.Enemy.LootTables.*;

/**
 * a basic form of Vampire in the world
 */
public class Vampire extends BasicEnemy {

    private BasicMove normalMove;
    private BasicMove critMove;

    private BasicLootTable lootTable;

    private final int BATTLE_RADIUS = 3;
    private final int SUPPORT_RADIUS = 4;
    private final int EXP = 50;
    private final int GOLD = 20;

    public Vampire(PathPosition position) {
        super(position);
        super.setHealth(75);
        super.setType("Vampire");
        this.critMove = new VampireCritHit();
        this.normalMove = new VampireHit();
        this.lootTable = new VampireLootTable();
    }

    @Override
    public BasicMove attack(long seed) {
        // 20% chance of landing a critical hit
        Random rng = new Random(seed);
        if (rng.nextInt(5) == 0) {
            return this.critMove;
        } else {
            return this.normalMove;
        }
    }

    @Override
    public int getBattleRadius() {
        return BATTLE_RADIUS;
    }

    @Override
    public int getSupportRadius() {
        return SUPPORT_RADIUS;
    }

    @Override
    public StaticEntity dropBuildingLoot() {
        return this.lootTable.generateBuilding();
    }

    @Override
    public StaticEntity dropItemLoot() {
        return this.lootTable.generateItem();
    }

    @Override
    public int dropExp() {
        return EXP;
    }

    @Override
    public int dropGold() {
        return GOLD;
    }
}
