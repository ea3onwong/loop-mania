package unsw.loopmania.card;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Card;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.card.LootTables.*;

/**
 * represents a Campfire Card in the backend game world
 */
public class CampfireCard extends Card {

    private CardLootTable lootTable;
    private final int EXP = 30;
    private final int GOLD = 30;

    public CampfireCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.lootTable = new EpicLootTable();
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
