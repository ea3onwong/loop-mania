/**
 * Written by Gordon Wang z5309206
 */
package unsw.loopmania.Enemy.EnemyMoves;

/**
 * Basic Zombie hit
 */
public class ZombieHit implements BasicMove {

    private int damage;

    public ZombieHit() {
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
