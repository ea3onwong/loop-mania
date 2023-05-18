/**
 * Written by Gordon Wang z5309206
 */
package unsw.loopmania.Enemy.EnemyMoves;

/**
 * Basic Thief hit that will steal the player's items
 * This attack is harmless
 */
public class ThiefSwipe implements BasicMove {

    private int damage;

    public ThiefSwipe() {
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
