package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Items.DefendStrategies.ShieldDefendStrategy;

/**
 * This class represents an equipped or unequipped shield in the backend world.
 */
public class Shield extends Wearable {

    public Shield(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setDefend(new ShieldDefendStrategy());
    }

    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "shield";
    }
    
}
