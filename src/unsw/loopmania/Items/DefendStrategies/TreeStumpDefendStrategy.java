package unsw.loopmania.Items.DefendStrategies;

import unsw.loopmania.Boss.Boss;
import unsw.loopmania.Enemy.BasicEnemy;

/**
 * Added by Adam.
 * This is a class that represents the tree stump's behaviour.
 */
public class TreeStumpDefendStrategy implements DefendStrategy {

    public int defend(BasicEnemy enemy, int damage) {
        if (enemy instanceof Boss) {
            if (damage <= 10) {
                return 0;
            } else {
                return damage - 10;
            }
        } else {
            if (damage <= 5) {
                return 0;
            } else {
                return damage - 5;
            }
        }
    }
    
    @Override
    public String toString() {
        return "tree_stump";
    }
}
