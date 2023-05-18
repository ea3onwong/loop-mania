/**
 * Written by Gordon Wang z5309206
 */
package unsw.loopmania.Enemy.EnemyMoves;

/**
 * Basic Vampire hit
 */
public class VampireHit implements BasicMove {

    private int damage;

    public VampireHit() {
        this.damage = 10;
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
