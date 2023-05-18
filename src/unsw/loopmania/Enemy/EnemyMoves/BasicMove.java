/**
 * Written by Gordon Wang z5309206
 * This file is the interface for all moves that an enemy can do
 */
package unsw.loopmania.Enemy.EnemyMoves;

public interface BasicMove {

    /**
     * Sets the damage that this move does
     */
    public void setDamage(int newDamage);

    /**
     * Gets the damage that this move does
     */
    public int getDamage();

}
