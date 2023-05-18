package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Items.AttackStrategies.StaffStrategy;

/**
 * This class represents an equipped or unequipped staff in the backend world.
 */
public class Staff extends Weapon {

    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setAttack(new StaffStrategy());
    }
    
    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "staff";
    }
}
