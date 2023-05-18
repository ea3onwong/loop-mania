package unsw.loopmania.card;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Card;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.card.LootTables.*;

/**
 * represents a Alchemist's Tent Card in the backend game world
 */
public class AlchemistTentCard extends Card {

    private CardLootTable lootTable;
    private final int EXP = 20;
    private final int GOLD = 20;

    public AlchemistTentCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.lootTable = new RareLootTable();
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