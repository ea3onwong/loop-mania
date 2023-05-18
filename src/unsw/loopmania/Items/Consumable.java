package unsw.loopmania.Items;
/**
 * Written by Adam
 */
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.StaticEntity;

public abstract class Consumable extends StaticEntity {

    public Consumable(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "";
    }
}
