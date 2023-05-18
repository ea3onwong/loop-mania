package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;


/**
 * Field Kitchen Building which the character can get 2 rations from it
 */
public class FieldKitchenBuilding extends Building {

    public FieldKitchenBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public void performAbility(Character character) {
        //System.out.println("The character cooked in Field Kitchen and got 2 rations");
    }

    @Override
    public int getRadius() {
        return 1;
    }
}