package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a TheOneRing item in the backend world
 */
public class TheOneRing extends Consumable {

    public TheOneRing(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }
    
    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "the_one_ring";
    }
}
