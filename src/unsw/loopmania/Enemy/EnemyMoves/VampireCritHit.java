/**
 * Written by Gordon Wang z5309206
 */
package unsw.loopmania.Enemy.EnemyMoves;

/**
 * Critical vampire hit that does increased damage
 */
public class VampireCritHit implements BasicMove {

    private int damage;

    public VampireCritHit() {
        this.damage = 20;
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
