package unsw.loopmania.Boss;

import java.util.Random;

import unsw.loopmania.PathPosition;
import unsw.loopmania.Enemy.BasicEnemy;
import unsw.loopmania.Enemy.EnemyMoves.BasicMove;
import unsw.loopmania.Enemy.EnemyMoves.CryptoHit;
import unsw.loopmania.Enemy.EnemyMoves.HealHit;

public class ElanMuske extends BasicEnemy implements Boss {

    private final int BATTLE_RADIUS = 2;
    private final int SUPPORT_RADIUS = 2;

    private BasicMove normalMove;
    private BasicMove critMove;

    public ElanMuske(PathPosition position) {
        super(position);
        super.setHealth(300);
        this.critMove = new HealHit();
        this.normalMove = new CryptoHit();
    }

    @Override
    public int affectTheCryptoPrices() {
        // Elan Muske's presence will always positively increase the crypto prices
        int maxFluctuation = 1200;
        int minFluctuation = 1000;
        Random rng = new Random();
        int fluctuation = rng.nextInt(maxFluctuation - minFluctuation) + minFluctuation;
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
        // Elan Muske may drop multiple Crypto
        Random rng = new Random();
        return rng.nextInt(5) + 1;
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
        // Elan will always be on the move
        int directionChoice = rng.nextInt(10);
        if (directionChoice % 2 == 0){
            moveUpPath();
        } else {
            moveDownPath();
        }
    }

    /**
     * Elan's attacks will deal a lot more damage if the crypto prices
     * are high
     */
    public void changeAttackDamage(int cryptoPrice) {
        if (cryptoPrice < 0) {
            cryptoPrice *= -1;
        }
        // Divides the cryptoprice by 10
        int newDmg = Math.floorDiv(cryptoPrice, 10);
        this.normalMove.setDamage(newDmg);
    }

}
