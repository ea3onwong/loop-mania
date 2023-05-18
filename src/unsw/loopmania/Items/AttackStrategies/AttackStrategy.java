package unsw.loopmania.Items.AttackStrategies;

import unsw.loopmania.Enemy.BasicEnemy;

/**
 * This is an interface that represents the defending behaviour.
 */
public interface AttackStrategy {
    
    /**
     * Attacks the enemy
     * @param enemy
     * @return the attack damage to enemy.
     */
    public int attack(BasicEnemy enemy);
}
