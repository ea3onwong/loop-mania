package unsw.loopmania.Boss;

import java.util.Random;

import unsw.loopmania.PathPosition;
import unsw.loopmania.Enemy.BasicEnemy;
import unsw.loopmania.Enemy.EnemyMoves.BasicMove;
import unsw.loopmania.Enemy.EnemyMoves.DoggieHit;
import unsw.loopmania.Enemy.EnemyMoves.StunHit;

public class Doggie extends BasicEnemy implements Boss {

    private final int BATTLE_RADIUS = 2;
    private final int SUPPORT_RADIUS = 2;

    private BasicMove normalMove;
    private BasicMove critMove;

    public Doggie(PathPosition position) {
        super(position);
        super.setHealth(105);
        this.critMove = new StunHit();
        this.normalMove = new DoggieHit();
    }

    @Override
    public int affectTheCryptoPrices() {
        // Doggie will fluctuate cypto prices either positively or
        // negatively
        Random rng = new Random(System.currentTimeMillis());
        int fluctuation = rng.nextInt(151);
        if (rng.nextInt(2) == 0) {
            // 50% chance to flip the fluctuation
            fluctuation *= -1;
        }
        return fluctuation;
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
    public int dropCrypto() {
        // Doggie always drops one coin
        return 1;
    }

    @Override
    public BasicMove attack(long seed) {
        // 20% chance of landing a critical hit
        Random rng = new Random(seed);
        if (rng.nextInt(5) == 0) {
            return this.critMove;
        } else {
            return this.normalMove;
        }
    }

    @Override
    public void move(long seed) {
        // Doggie moves a lot and is more unpredictable
        // Moves twice in one move
        Random rng;
        if (seed == -1) {
            rng = new Random();
        } else {
            rng = new Random(seed);
        }
        for (int loop = 0; loop < 2; loop++) {
            int directionChoice = rng.nextInt(11);
            if (directionChoice % 2 == 0){
                moveUpPath();
            } else {
                moveDownPath();
            }
        }
    }
    
}
