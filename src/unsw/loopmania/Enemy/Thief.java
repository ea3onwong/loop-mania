/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.Enemy;

import java.util.Random;

import unsw.loopmania.PathPosition;
import unsw.loopmania.Enemy.EnemyMoves.*;

/**
 * a basic form of Zombie in the world
 */
public class Thief extends BasicEnemy {

    private BasicMove normalMove;
    
    private final int BATTLE_RADIUS = 2;
    // A Thief is standalone and does not support anyone
    private final int SUPPORT_RADIUS = 0;
    private final int EXP = 20;
    // The gold that the thief has stolen
    private int stolenGold;

    public Thief(PathPosition position, int stolenGold) {
        super(position);
        super.setHealth(25);
        super.setType("Thief");
        this.normalMove = new ThiefSwipe();
        this.stolenGold = stolenGold;
    }

    @Override
    public void move(long seed) {
        // A thief is always on the move and running
        // away from the player. However, may stay still
        // to let the player catch up (33.33% to stay still)
        Random rng = null;
        if (seed == -1) {
            rng = new Random();
        } else {
            rng = new Random(seed);
        }
        
        if (rng.nextInt(3) != 1) {
            moveDownPath();
        }
    }

    @Override
    public BasicMove attack(long seed) {
        // Only has one move and that steals items
        return this.normalMove;
    }

    @Override
    public int getBattleRadius() {
        return BATTLE_RADIUS;
    }

    @Override
    public int getSupportRadius() {
        return SUPPORT_RADIUS;
    }

    @Override
    public int dropExp() {
        return EXP;
    }

    @Override
    public int dropGold() {
        return this.stolenGold;
    }

}
