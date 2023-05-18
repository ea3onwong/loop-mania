package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;


/**
 * Holy Spring Building which can recover character by his full HP
 */
public class HolySpringBuilding extends Building {

    public HolySpringBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public void performAbility(Character character) {
        if (character.getIntHP() < character.getIntMaxHP()) {
            System.out.println("The character has drunk the Holy Spring");
            character.changeHealth(character.getIntMaxHP());
            System.out.println("The character has got healed by FULL HP, now the character is " + character.getIntHP());
        } else {
            System.out.println("The character passes through the Holy Spring");
        }
        
    }

    @Override
    public int getRadius() {
        return 1;
    }
}
