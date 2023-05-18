/**
 * Written by Gordon Wang z5309206
 */
package unsw.loopmania.Enemy.EnemyMoves;

/**
 * Basic hit that changes depending on the Crypto prices
 */
public class CryptoHit implements BasicMove {

    private int damage;

    public CryptoHit() {
        this.damage = 50;
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
