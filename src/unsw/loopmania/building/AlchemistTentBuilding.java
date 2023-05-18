package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;


/**
 * Alchemist's Tent Building which can increase the character BASE_ATK by 5 
 */
public class AlchemistTentBuilding extends Building {

    public AlchemistTentBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public void performAbility(Character character) {
        System.out.println("The character has entered the Alchemist's Tent, now the character's attack is " + character.getIntAttack() + ", base attack is " + character.getIntBaseATK());
        character.changeBaseATK(5);
        System.out.println("The character got help from the Alchemist, now the character's attack is " + character.getIntAttack() + ", base attack is " + character.getIntBaseATK());
    }

    @Override
    public int getRadius() {
        return 1;
    }
}
