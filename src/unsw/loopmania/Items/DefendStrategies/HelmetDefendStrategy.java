package unsw.loopmania.Items.DefendStrategies;

import unsw.loopmania.Enemy.BasicEnemy;

/**
 * Added by Adam
 * This is a class that represents the Helmet's behaviour. 
 */
public class HelmetDefendStrategy implements DefendStrategy{
    
    /**
     * This method is used to reduce enemy's damage regardless of the enemy's type by 5.
     * @param enemy         The enemy. 
     * @param damage        The damage cause by the enemy.
     * @return              The real damage after defending by Helmet. 
     */
    public int defend(BasicEnemy enemy, int damage) {
        if (damage <= 5) {
            return 0;
        } else {
            return damage - 5;
        }
    }
    
    @Override
    public String toString() {
        return "helmet";
    }
}
