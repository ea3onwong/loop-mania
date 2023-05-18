package unsw.loopmania.Items.AttackStrategies;

import unsw.loopmania.Boss.Boss;
import unsw.loopmania.Enemy.BasicEnemy;

/**
 * This is an interface that represents the attacking behaviour.
 */
public class AndurilStrategy implements AttackStrategy {
    
    @Override
    public int attack(BasicEnemy enemy) {
        if (enemy instanceof Boss) {
            return 45;
        } else {
            return 15;
        }
    }

    @Override
    public String toString() {
        return "anduril_flame_of_the_west";
    }
}
