package unsw.loopmania.Items.AttackStrategies;

import unsw.loopmania.Enemy.BasicEnemy;

/**
 * This is an interface that represents the attacking behaviour.
 */
public class StakeStrategy implements AttackStrategy {
    
    @Override
    public int attack(BasicEnemy enemy) {
        if (enemy.getType().equals("Vampire")) {
            return 20;
        } else {
            return 5;
        }
    }

    @Override
    public String toString() {
        return "stake";
    }
}
