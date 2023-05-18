package unsw.loopmania.Items.AttackStrategies;

import unsw.loopmania.Enemy.BasicEnemy;

/**
 * This is an interface that represents the attacking behaviour.
 */
public class SwordStrategy implements AttackStrategy {
    
    @Override
    public int attack(BasicEnemy enemy) {
        return 10;
    }

    @Override
    public String toString() {
        return "sword";
    }
}
