package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;


/**
 * a basic form of building in the world
 */
public class CampfireBuilding extends Building {
    public CampfireBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public void performAbility(Character character) {
        // Campfire empowers the character, making them deal double damage
        character.doubleAttack(2);
        System.out.println("Now the damage of character can deal is doubled");
    }

    @Override
    public int getRadius() {
        return 3;
    }
}
