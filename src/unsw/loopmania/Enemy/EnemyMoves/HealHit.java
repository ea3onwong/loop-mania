/**
 * Written by Gordon Wang z5309206
 */
package unsw.loopmania.Enemy.EnemyMoves;

/**
 * A hit that deals no damage but heals all supporting enemies
 */
public class HealHit implements BasicMove {

    private int damage;

    public HealHit() {
        this.damage = 0;
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
