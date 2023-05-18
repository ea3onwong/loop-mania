package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Items.AttackStrategies.SwordStrategy;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Sword extends Weapon {

    public Sword(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setAttack(new SwordStrategy());
    }
    
    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "sword";
    }
}
