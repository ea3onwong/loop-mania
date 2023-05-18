package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Items.DefendStrategies.ArmourDefendStrategy;

/**
 * This class represents an equipped or unequipped armour in the backend world.
 */
public class Armour extends Wearable {

    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setDefend(new ArmourDefendStrategy());
    }

    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "armour";
    }

}
