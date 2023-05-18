package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;

/**
 * HeroCastle in the world
 */
public class HeroCastleBuilding extends Building {

    public HeroCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public void performAbility(Character character) {
        character.increaseTotalCycles();
    }

    @Override
    public int getRadius() {
        return 1;
    }
}
