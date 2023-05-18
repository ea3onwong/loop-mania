package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a potion item in the backend world
 */
public class Potion extends Consumable {

    public Potion(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }
    
    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "potion";
    }
}
