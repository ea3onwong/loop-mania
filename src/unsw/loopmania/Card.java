package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * a Card in the world
 * which doesn't move
 */
public abstract class Card extends StaticEntity {
    
    public Card(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    /**
     * Generates random item loot
     * @return
     */
    public StaticEntity dropItemLoot() {
        return null;
    }

    /**
     * Get the characters experience drop
     */
    public int dropExp() {
        return 0;
    }

    /**
     * Get the characters gold drop
     */
    public int dropGold() {
        return 0;
    }
}
