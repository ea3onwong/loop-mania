package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a Ration item in the backend world
 */
public class Ration extends Consumable {

    public Ration(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }
    
    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "ration";
    }
}
