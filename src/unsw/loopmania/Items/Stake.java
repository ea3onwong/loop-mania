package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Items.AttackStrategies.StakeStrategy;

/**
 * This class represents an equipped or unequipped stake in the backend world.
 */
public class Stake extends Weapon {

    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setAttack(new StakeStrategy());
    }

    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "stake";
    }    
    
}
