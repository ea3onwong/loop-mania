package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;

/**
 * a basic form of building in the world
 */
public class BarracksBuilding extends Building {

    public BarracksBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public void performAbility(Character character) {
        if (character.getIntAllies() < 3) {
            character.changeAlly(1);
            if (character.getIntAllies() == 1) {
                System.out.println("The character entered the barracks and one allied soldier join in the adventure, now has 1 allied soldier");
            } else if (character.getIntAllies() < 3) {
                System.out.println("The character entered the barracks and one allied soldier join in the adventure, now has 2 allied soldiers");
            } else if (character.getIntAllies() == 3) {
                System.out.println("The character entered the barracks and one allied soldier join in the adventure, now has 3 allied soldiers which reach the max");
            }
        } else {
            character.changeAlly(1);
            System.out.println("The character entered the barracks, now has 3 allied soldiers which reach the max, no allied soldier join in the adventure");
        }
        
    }

    @Override
    public int getRadius() {
        return 1;
    }
}
