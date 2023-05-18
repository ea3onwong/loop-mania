package unsw.loopmania.Items;

import java.util.ArrayList;
import java.util.Random;

import unsw.loopmania.Entity;
import unsw.loopmania.Items.AttackStrategies.AndurilStrategy;
import unsw.loopmania.Items.AttackStrategies.AttackStrategy;
import unsw.loopmania.Items.DefendStrategies.DefendStrategy;
import unsw.loopmania.Items.DefendStrategies.TreeStumpDefendStrategy;

public class RareItemProperties {
    
    DefendStrategy andurilDefendStrategy;
    AttackStrategy andurilAttackStrategy;

    DefendStrategy oneRingDefendStrategy;
    AttackStrategy oneRingAttackStrategy;

    DefendStrategy treeStumpDefendStrategy;
    AttackStrategy treeStumpAttackStrategy;

    ArrayList<Entity> revivableItem;

    public RareItemProperties() {
        // Setting up anduril
        this.andurilDefendStrategy = null;
        this.andurilAttackStrategy = new AndurilStrategy();

        // Setting up one ring        
        this.oneRingDefendStrategy = null;
        this.oneRingAttackStrategy = null;
        this.revivableItem = new ArrayList<Entity>();
        this.revivableItem.add(new TheOneRing(null, null));

        // Setting up tree stump
        this.treeStumpAttackStrategy = null;
        this.treeStumpDefendStrategy = new TreeStumpDefendStrategy();
    }

    public void resetProperties() {
        // Restting anduril
        this.andurilDefendStrategy = null;
        this.andurilAttackStrategy = new AndurilStrategy();

        // Restting one ring        
        this.oneRingDefendStrategy = null;
        this.oneRingAttackStrategy = null;
        this.revivableItem.clear();
        this.revivableItem.add(new TheOneRing(null, null));

        // Restting tree stump
        this.treeStumpAttackStrategy = null;
        this.treeStumpDefendStrategy = new TreeStumpDefendStrategy();
    }

    /**
     * Randomise the properties by adding
     * @param seed
     */
    public void randomiseProperties(long seed) {
        resetProperties();
        Random rng = new Random(seed);

        // Anduril has a 50% chance of getting tree stump properties
        if (rng.nextInt(2) == 0) {
            this.andurilDefendStrategy = new TreeStumpDefendStrategy();
        }

        // Tree stump has a 50% chance of getting Anduril properties
        if (rng.nextInt(2) == 0) {
            this.treeStumpAttackStrategy = new AndurilStrategy();
        }

        // One ring has either 50% chance of getting anduril or tree stump properties
        if (rng.nextInt(2) == 0) {
            this.oneRingAttackStrategy = new AndurilStrategy();
        } else {
            this.oneRingDefendStrategy = new TreeStumpDefendStrategy();
        }

        // Either anduril or tree stump can revive the player
        if (rng.nextInt(2) == 0) {
            this.revivableItem.add(new Anduril(null, null));
        } else {
            this.revivableItem.add(new TreeStump(null, null));
        }
    }

    /**
     * Getting Anduril's defend strategies
     * @return DefendStrategy
     */
    public DefendStrategy getAndurilDefend() {
        return this.andurilDefendStrategy;
    }

    /**
     * Getting Anduril's attack strategies
     * @return AttackStrategy
     */
    public AttackStrategy getAndurilAttack() {
        return this.andurilAttackStrategy;
    }

    /**
     * Getting One Ring's defend strategies
     * @return DefendStrategy
     */
    public DefendStrategy getOneRingDefend() {
        return this.andurilDefendStrategy;
    }

    /**
     * Getting One Ring's attack strategies
     * @return AttackStrategy
     */
    public AttackStrategy getOneRingAttack() {
        return this.andurilAttackStrategy;
    }

    /**
     * Getting Tree Stump's defend strategies
     * @return DefendStrategy
     */
    public DefendStrategy getTreeStumpDefend() {
        return this.andurilDefendStrategy;
    }

    /**
     * Getting Tree Stump's attack strategies
     * @return AttackStrategy
     */
    public AttackStrategy getTreeStumpAttack() {
        return this.andurilAttackStrategy;
    }

    /**
     * Returns true if the item can revive player
     * False if otherwise
     * @param item
     * @return
     */
    public boolean canThisRevivePlayer(Entity item) {
        for (Entity revivable : this.revivableItem) {
            if (revivable.getClass().equals(item.getClass())) {
                return true;
            }
        }
        return false;
    }
}
