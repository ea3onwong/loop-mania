package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;


/**
 * Survivors' Tent Building which can increase the character MAX_HP by 5 
 */
public class SurvivorsTentsBuilding extends Building {

    public SurvivorsTentsBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public void performAbility(Character character) {
        System.out.println("The character has entered the Survivors' Tent, now the character's max health is " + character.getIntMaxHP() + " health is " + character.getIntHP());
        character.changeMaxHealth(5);
        System.out.println("The character got help from the Survivors, now the character's max health is " + character.getIntMaxHP() + " health is " + character.getIntHP());
    }

    @Override
    public int getRadius() {
        return 1;
    }
}