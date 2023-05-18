package unsw.loopmania.building;


import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Enemy.BasicEnemy;

/**
 * a basic form of building in the world
 */
public class TrapBuilding extends Building {
    public TrapBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        super.setType("Trap");
    }
    
    @Override
    public void performAbility(BasicEnemy enemy) {
        // Trap deals 20 damage to enemies that walk into them, then the trap is destroyed
        enemy.loseHealth(20);
        if (enemy.getHealth() > 0) {
            System.out.println("The " + enemy.getType() + " step on the trap and get 20 damage, now it has " + enemy.getHealth() + " HP");
        } else {
            System.out.println("The " + enemy.getType() + " step on the trap and get 20 damage and dead");
        }
    }

    @Override
    public int getRadius() {
        return 1;
    }
}

