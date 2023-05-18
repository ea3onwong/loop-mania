/**
 * Written by z5309206
 * This determines the drop rate of items
 */
package unsw.loopmania.Enemy.LootTables;

import unsw.loopmania.StaticEntity;

public interface BasicLootTable {

    public StaticEntity generateItem();

    public StaticEntity generateBuilding();

}

