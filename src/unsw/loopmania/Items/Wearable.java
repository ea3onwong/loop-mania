/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.Items;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.Items.DefendStrategies.DefendStrategy;

public abstract class Wearable extends StaticEntity {

    private DefendStrategy defence;

    public Wearable(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "";
    }

    public void setDefend(DefendStrategy defence) {
        this.defence = defence;
    }

    public DefendStrategy getDefend() {
        return this.defence;
    }
    
}
