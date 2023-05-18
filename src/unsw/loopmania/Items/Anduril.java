package unsw.loopmania.Items;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Items.AttackStrategies.AndurilStrategy;

/**
 * represents an equipped or unequipped Anduril in the backend world
 */
public class Anduril extends Weapon {

    public Anduril(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setAttack(new AndurilStrategy());
    }
    
    /**
     * Used to identify what item this is
     */
    @Override
    public String toString() {
        return "anduril_flame_of_the_west";
    }
}
