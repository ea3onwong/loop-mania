package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;


public class PotionPileBuilding extends Building {
    public PotionPileBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }
    
    @Override
    public void performAbility(Character character) {
        // Heals the player by 5hp
        character.changeHealth(5);
    }

    @Override
    public int getRadius() {
        return 1;
    }
}