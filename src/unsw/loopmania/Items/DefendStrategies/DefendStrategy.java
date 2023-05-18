package unsw.loopmania.Items.DefendStrategies;

import unsw.loopmania.Enemy.BasicEnemy;

/**
 * Added by Eason
 * This is an interface that represents the defending behaviour.
 */
public interface DefendStrategy {
    /**
     * This method is used to reduce the enemy's damage to the character. 
     * @param enemy     The enemy 
     * @param damage    Enemy's original damage. 
     * @return          The real damage after defending. 
     */
    public int defend(BasicEnemy enemy, int damage);
}
