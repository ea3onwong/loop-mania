/**
 * Written by Gordon Wang z5309206
 */
package unsw.loopmania.Enemy.EnemyMoves;

/**
 * Basic slug hit
 */
public class SlugHit implements BasicMove {

    private int damage;

    public SlugHit() {
        this.damage = 2;
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
