package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Items.DefendStrategies.HelmetDefendStrategy;

/**
 * This class represents an equipped or unequipped helmet in the backend world.
 */
public class Helmet extends Wearable {

    public Helmet(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setDefend(new HelmetDefendStrategy());
    }

    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "helmet";
    }

}
