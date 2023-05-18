package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Items.DefendStrategies.TreeStumpDefendStrategy;

/**
 * This class represents an equipped or unequipped TreeStump in the backend world.
 */
public class TreeStump extends Wearable {

    public TreeStump(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setDefend(new TreeStumpDefendStrategy());
    }

    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "tree_stump";
    }
    
}
