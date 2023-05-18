package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;

/**
 * a basic form of building in the world
 */
public class TowerBuilding extends Building {
    public TowerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }
    
    @Override
    public void performAbility(Character character) {
        // Tower aids the character in battle, for now it gives the character a damage bonus
        character.changeAttack(20);
        System.out.println("Now the damage of character can deal is plussed by 20");
    }

    @Override
    public int getRadius() {
        return 3;
    }
}