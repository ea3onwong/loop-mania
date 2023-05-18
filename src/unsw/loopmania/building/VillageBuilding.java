package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;


/**
 * a basic form of building in the world
 */
public class VillageBuilding extends Building {

    public VillageBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public void performAbility(Character character) {
        System.out.println("The character has entered the Village");
        character.changeHealth(20);
        System.out.println("The character has got healed by 20 hp, now the character is " + character.getIntHP());
    }

    @Override
    public int getRadius() {
        return 1;
    }
}
