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
 * a basic form of Zombie in the world
 */
public class Zombie extends BasicEnemy {

    private BasicMove normalMove;
    private BasicMove critMove;

    private BasicLootTable lootTable;

    private final int BATTLE_RADIUS = 2;
    private final int SUPPORT_RADIUS = 2;
    private final int EXP = 10;
    private final int GOLD = 5;

    public Zombie(PathPosition position) {
        super(position);
        super.setHealth(25);
        super.setType("Zombie");
        this.critMove = new ZombieCritHit();
        this.normalMove = new ZombieHit();
        this.lootTable = new ZombieLootTable();
    }

    @Override
    public void move(long seed) {
        // A Zombie is much slower than the normal enemy.
        // 90% Do nothing, 5% move up, 5% move down
        Random rng;
        if (seed == -1) {
            rng = new Random();
        } else {
            rng = new Random(seed);
        }
        int directionChoice = rng.nextInt(10);
        if (directionChoice == 0){
            if (rng.nextInt(2) == 0) {
                moveDownPath();
            } else {
                moveUpPath();
            }
        }
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
