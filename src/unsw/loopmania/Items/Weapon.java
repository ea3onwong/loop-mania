/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.Items;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.AttackStrategies.AttackStrategy;

public abstract class Weapon extends StaticEntity {

    private AttackStrategy attack;

    public Weapon(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "";
    }

    public void setAttack(AttackStrategy attack) {
        this.attack = attack;
    }

    public AttackStrategy getAttack() {
        return this.attack;
    }

}
