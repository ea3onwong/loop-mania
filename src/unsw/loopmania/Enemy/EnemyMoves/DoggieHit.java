/**
 * Written by Gordon Wang z5309206
 */
package unsw.loopmania.Enemy.EnemyMoves;

/**
 * Basic Doggie hit
 */
public class DoggieHit implements BasicMove {

    private int damage;

    public DoggieHit() {
        this.damage = 30;
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
