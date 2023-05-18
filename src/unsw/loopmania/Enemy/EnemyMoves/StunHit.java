/**
 * Written by Gordon Wang z5309206
 */
package unsw.loopmania.Enemy.EnemyMoves;

/**
 * Basic Doggie hit that does not deal much damage but stuns the character
 * so that they can't move in the next round
 */
public class StunHit implements BasicMove {

    private int damage;

    public StunHit() {
        this.damage = 15;
    }

    @Override
    public void setDamage(int newDamage) {
        this.damage = newDamage;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }
    
}
