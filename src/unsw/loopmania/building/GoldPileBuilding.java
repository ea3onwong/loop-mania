package unsw.loopmania.building;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;


public class GoldPileBuilding extends Building {
    public GoldPileBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }
    
    @Override
    public void performAbility(Character character) {
        // Randomly gives gold
        Random rng = new Random(System.currentTimeMillis());
        int totalGold = rng.nextInt(5) + 1;
        character.changeGold(totalGold);
    }

    @Override
    public int getRadius() {
        return 1;
    }
}