/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Character;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.Enemy.BasicEnemy;

public abstract class Building extends StaticEntity {

    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Building(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public int getRadius() {
        return 0;
    }

    /**
     * Perform the buildings ability on the given character
     * @param character
     */
    public void performAbility(Character character) {}

    /**
     * Perform the buildings ability on the given enemy
     * @param enemy
     */
    public void performAbility(BasicEnemy enemy) {}


}
