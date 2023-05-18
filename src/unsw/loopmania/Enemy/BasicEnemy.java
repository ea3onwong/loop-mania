package unsw.loopmania.Enemy;

import java.util.Random;

import unsw.loopmania.MovingEntity;
import unsw.loopmania.PathPosition;
import unsw.loopmania.StaticEntity;
import unsw.loopmania.Enemy.EnemyMoves.*;

/**
 * a basic form of enemy in the world
 */
public abstract class BasicEnemy extends MovingEntity {

    private int health;
    private String type;
    private boolean isTranced;
    private int totalTranceTimes;

    public BasicEnemy(PathPosition position) {
        super(position);
        this.health = 10;
        this.isTranced = false;
        this.totalTranceTimes = 0;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // check if the enemy is tranced by the player.
    public boolean isTranced() {
        return this.isTranced;
    }

    public void setTranceStatus(boolean isTranced) {
        this.isTranced = isTranced;
    }

    // get how many rounds of fight that the enemy is tranced by the player.
    public int getTotalTranceTimes() {
        return totalTranceTimes;
    }

    public void addInflictTranceTimes() {
        this.totalTranceTimes++;
    }

    /**
     * move the enemy
     * Seed = -1 to use a the system's time seed
     */
    public void move(long seed){
        // this basic enemy moves in a random direction... 25% chance up or down, 50% chance not at all...
        Random rng;
        if (seed == -1) {
            rng = new Random();
        } else {
            rng = new Random(seed);
        }

        int directionChoice = rng.nextInt(2);
        if (directionChoice == 1){
            directionChoice = rng.nextInt(2);
            if (directionChoice == 0) {
                moveUpPath();
            } else {
                moveDownPath();
            }
            
        }
    }

    /**
     * Deducts health points from this enemy
     * @param lost
     */
    public void loseHealth(int lost) {
        this.health -= lost;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    /**
     * Gets this enemy's current health
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Changes the enemy's health to be the newHealth
     * @param newHealth
     */
    public void setHealth(int newHealth) {
        this.health = newHealth;
    }

    /**
     * Returns a move
     * @return
     */
    public BasicMove attack(long seed) {
        return new SlugHit();
    }

    /**
     * Gets the battle radius of this enemy
     * @return int
     */
    public int getBattleRadius() {
        return 0;
    }

    /**
     * Gets the support radius of this enemy
     * @return 0
     */
    public int getSupportRadius() {
        return 0;
    }
    
    /**
     * Generates random building card loot
     * @return
     */
    public StaticEntity dropBuildingLoot() {
        return null;
    }

    /**
     * Generates random item loot
     * @return
     */
    public StaticEntity dropItemLoot() {
        return null;
    }

    /**
     * Get the characters experience drop
     */
    public int dropExp() {
        return 0;
    }

    /**
     * Get the characters gold drop
     */
    public int dropGold() {
        return 0;
    }

}
