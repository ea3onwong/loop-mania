/**
 * Written by Gordon Wang z5309206
 */
package unsw.loopmania.Enemy.EnemyMoves;

/**
 * Crit hit should spawn a zombie and remove an ally
 */
public class ZombieCritHit implements BasicMove {

    private int damage;

    public ZombieCritHit() {
        this.damage = 5;
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
