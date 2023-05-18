/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.Enemy;

import unsw.loopmania.PathPosition;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.Enemy.EnemyMoves.*;
import unsw.loopmania.Enemy.LootTables.*;

/**
 * a basic form of Slug in the world
 */
public class Slug extends BasicEnemy {

    private BasicMove normalMove;

    private BasicLootTable lootTable;

    private final int BATTLE_RADIUS = 2;
    private final int SUPPORT_RADIUS = 2;
    private final int EXP = 1;
    private final int GOLD = 1;

    public Slug(PathPosition position) {
        super(position);
        super.setHealth(10);
        super.setType("Slug");
        this.normalMove = new SlugHit();
        this.lootTable = new SlugLootTable();
    }

    @Override
    public BasicMove attack(long seed) {
        // Slugs can only do one move
        return this.normalMove;
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
