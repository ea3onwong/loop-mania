package unsw.loopmania;

import java.util.ArrayList;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Enemy.BasicEnemy;
import unsw.loopmania.Items.Weapon;
import unsw.loopmania.Items.Wearable;
import unsw.loopmania.Items.AttackStrategies.AttackStrategy;
import unsw.loopmania.Items.DefendStrategies.DefendStrategy;
import unsw.loopmania.Items.DefendStrategies.ShieldDefendStrategy;
import unsw.loopmania.Items.DefendStrategies.TreeStumpDefendStrategy;
import unsw.loopmania.building.SpawnBuildingObserver;

/**
 * represents the main character in the backend of the game world
 */
public class Character extends MovingEntity implements CharacterBuildingSubject {

    /**
     * NOTE TO H15A_ECHO
     * Feel free to change these final ints, especially if you
     * feel the game is a little bit unbalanced. Let us know
     * if you do change it however.
     * - GW
     */
    private int MAX_HEALTH = 100;
    private final int MAX_ALLIES = 3;
    private int BASE_ATK = 2;
    private final int STARTING_GOLD = 5;

    private SimpleIntegerProperty health;
    private SimpleIntegerProperty gold;
    private SimpleIntegerProperty doggieCoins;
    private SimpleIntegerProperty exp;
    private SimpleIntegerProperty allies;
    private SimpleIntegerProperty attack;
    private int totalCycles;

    private boolean isElanSlain;
    private boolean isDoggieSlain;

    private LoopManiaWorldController controllerObserver;
    private ArrayList<SpawnBuildingObserver> spawnerObservers;
    
    // Added by Eason 
    private AttackStrategy attackStrategy;
    private DefendStrategy wearableStrategy;
    private DefendStrategy shieldStrategy;

    public Character(PathPosition position) {
        super(position);
        this.health = new SimpleIntegerProperty(MAX_HEALTH);
        this.gold = new SimpleIntegerProperty(STARTING_GOLD);
        this.doggieCoins = new SimpleIntegerProperty(0);
        this.exp = new SimpleIntegerProperty(0);
        this.allies = new SimpleIntegerProperty(0);
        this.attack = new SimpleIntegerProperty(BASE_ATK);
        this.spawnerObservers = new ArrayList<SpawnBuildingObserver>();
        this.attackStrategy = null;
        this.wearableStrategy = null;
        this.shieldStrategy = null;
        this.isDoggieSlain = false;
        this.isElanSlain = false;
    }

    /**
     * Stores the world controller with the character so that when the cycle
     * increases, the controller will be notified
     */
    public void setController(LoopManiaWorldController lwc) {
        this.controllerObserver = lwc;
    }
    
    /**
     * When a building that spawns is placed into the world, it is registered
     * to observe the character
     */
    @Override
    public void registerBuilding(SpawnBuildingObserver spawner) {
        this.spawnerObservers.add(spawner);   
    }

    /**
     * Notifies the observers that the character has completed a cycle
     */
    @Override
    public void notifyBuilding() {
        for (SpawnBuildingObserver building : this.spawnerObservers) {
            if (building.doesCycleMeet(this.totalCycles)) {
                building.setReadyToSpawn(true);
            }
        }
    }

    /**
     * ADDED CODE
     * Changes the boolean state of isDoggieSlain
     */
    public void setStateIsDoggieSlain(boolean state) {
        this.isDoggieSlain = state;
    }

    /**
     * ADDED CODE
     * Changes the boolean state of isElanSlain
     */
    public void setStateIsElanSlain(boolean state) {
        this.isElanSlain = state;
    }

    /**
     * ADDED CODE
     * Gets the boolean state of isDoggieSlain
     * @return boolean
     */
    public boolean getIsDoggieSlain() {
        return this.isDoggieSlain;
    }

    /**
     * ADDED CODE
     * Gets the boolean state of isElanSlain
     * @return boolean
     */
    public boolean getIsElanSlain() {
        return this.isElanSlain;
    }

    /**
     * Resets the character back to basic stats
     */
    public void resetCharacter() {
        // Keeps moving the character until they have reached back to the start
        // Of the path
        while (this.getCurrentPositionIndex() != 0) {
            this.moveDownPath();
        }
        this.health.set(MAX_HEALTH);
        this.gold.set(STARTING_GOLD);
        this.exp.set(0);
        this.allies.set(0);
        this.doggieCoins.set(0);
        this.attack.set(BASE_ATK);
        this.totalCycles = 0;
        this.isDoggieSlain = false;
        this.isElanSlain = false;
        this.spawnerObservers.clear();
        this.attackStrategy = null;
        this.wearableStrategy = null;
        this.shieldStrategy = null;
    }

    /**
     * Get total cycles
     * @return int
     */
    public int getIntTotalCycles() {
        return this.totalCycles;
    }

    /**
     * Increases total cycles of charcter by one
     */
    public void increaseTotalCycles() {
        this.totalCycles++;
        // Notifies the controller that the cycle has changed
        if (this.controllerObserver != null) {
            this.controllerObserver.notifyCycleChange(this.totalCycles);          
        }
        // Notifies the buildings that depend on the character cycles
        this.notifyBuilding();
    }

    /**
     * Gets the health property
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty getPropertyHP() {
        return this.health;
    }

    /**
     * Gets the character's health in the form of an int
     * @return int
     */
    public int getIntHP() {
        return this.health.get();
    }


    /**
     * Changes the health of the character
     * Negative numbers to decrease
     * Positive numbers to increase
     * @param healthPoints
     */
    public void changeHealth(int healthPoints) {
        int characterHealth = this.health.get();

        characterHealth += healthPoints;

        if (characterHealth > MAX_HEALTH) {
            characterHealth = MAX_HEALTH;
        } else if (characterHealth < 0) {
            characterHealth = 0;
        }

        this.health.setValue(characterHealth);
    }


    /**
     * Gets the doggie coin property
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty getPropertyDC() {
        return this.doggieCoins;
    }

    /**
     * Gets the character's doggie coin in the form of an int
     * @return int
     */
    public int getIntDoggieCoin() {
        return this.doggieCoins.get();
    }


    /**
     * Changes the total doggie coins of character
     * Negative numbers to decrease
     * Positive numbers to increase
     * @param healthPoints
     */
    public void changeDoggieCoin(int coins) {
        int characterCoin = this.doggieCoins.get() + coins;
        if (characterCoin < 0) {
            characterCoin = 0;
        }
        this.doggieCoins.set(characterCoin);
    }

    /**
     * Gets the character's health in the form of an int
     * @return int
     */
    public int getIntMaxHP() {
        return MAX_HEALTH;
    }


    /**
     * Changes the health of the character
     * Negative numbers to decrease
     * Positive numbers to increase
     * @param healthPoints
     */
    public void changeMaxHealth(int healthPoints) {
        int characterHealth = this.health.get();

        MAX_HEALTH += healthPoints;
        characterHealth += healthPoints;

        if (characterHealth > MAX_HEALTH) {
            characterHealth = MAX_HEALTH;
        } else if (characterHealth < 0) {
            characterHealth = 0;
        }

        this.health.setValue(characterHealth);
    }


    /**
     * Gets the property of gold
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty getPropertyGold() {
        return this.gold;
    }

    /**
     * Gets the gold in integer form
     * @return int
     */
    public int getIntGold() {
        return this.gold.get();
    }

    /**
     * Adds gold to the character
     * Use negative numbers to remove gold
     * @param gold
     */
    public void changeGold(int gold) {
        int characterGold = this.gold.get() + gold;
        if (characterGold < 0) {
            characterGold = 0;
        }
        this.gold.set(characterGold);
    }

    /**
     * Changes the character's gold
     * This does not add onto the characters gold, but instead
     * overrides the character's current gold
     * @param gold
     */
    public void setGold(int gold) {
        this.gold.set(gold);
    }

    /**
     * Gets the experience property
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty getPropertyExp() {
        return this.exp;
    }

    /**
     * Gets the experience of character in the form of an integer
     * @return int
     */
    public int getIntExp() {
        return this.exp.get();
    }

    /**
     * Changes the experience of character
     * @param exp
     */
    public void changeExp(int exp) {
        int characterExp = this.exp.get() + exp;
        if (characterExp < 0) {
            characterExp = 0;
        }
        this.exp.set(characterExp);
    }

    /**
     * Gets the ally property
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty getPropertyAlly() {
        return this.allies;
    }

    /**
     * Gets the total allies in an integer
     * @return int
     */
    public int getIntAllies() {
        return this.allies.get();
    }

    /**
     * Changes the ally
     * Negative number to remove ally
     * @param total
     */
    public void changeAlly(int total) {
        int currentTotal = this.allies.get() + total;
        if (currentTotal > MAX_ALLIES) {
            currentTotal = MAX_ALLIES;
        } else if (currentTotal < 0) {
            currentTotal = 0;
        }
        this.allies.set(currentTotal);
    }

    /**
     * Gets the character's attack property
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty getPropertyAttack() {
        return this.attack;
    }

    /**
     * Gets this character's attack value
     * @return
     */
    public int getIntAttack() {
        return this.attack.get();
    }

    /**
     * Changes the attack value of character
     * @param value
     */
    public void changeAttack(int value) {
        int currentAttack = this.attack.get();

        currentAttack += value;

        if (currentAttack < 0) {
            currentAttack = 0;
        }
        this.attack.setValue(currentAttack);
    }


    /**
     * Doubles the attack
     * @param size
     */
    public void doubleAttack(int size) {
        int currentAttack = this.attack.get() * size;
        if (currentAttack < 0) {
            currentAttack = 0;
        }
        this.attack.set(currentAttack);
    }

    /**
     * Gets the character's base attack in interger form
     * @return int
     */
    public int getIntBaseATK() {
        return BASE_ATK;
    }


    /**
     * Changes the base attack of the character
     * Negative numbers to decrease
     * Positive numbers to increase
     * @param attackValue
     */
    public void changeBaseATK(int attackValue) {
        int currentAttack = this.attack.get();

        BASE_ATK += attackValue;
        currentAttack += attackValue;

        if (currentAttack < 0) {
            currentAttack = 0;
        }
        this.attack.setValue(currentAttack);
    }

    /**
     * Added by Eason 
     * This is a basic setter method for the defend strategy
     */
    public void setDefendStrategy(Wearable item) {
        if (item.toString().equals("shield") || item.toString().equals("tree_stump")) {
            this.shieldStrategy = item.getDefend();
        } else {
            this.wearableStrategy = item.getDefend();
        }
    }

    /**
     * A more basic setter method for defend strategy
     * Allows to put in the strategy directly
     * @param defendStrat
     */
    public void setDefendStrategy(DefendStrategy defendStrat) {
        if (defendStrat instanceof TreeStumpDefendStrategy || defendStrat instanceof ShieldDefendStrategy) {
            this.shieldStrategy = defendStrat;
        } else {
            this.wearableStrategy = defendStrat;
        }
    }

    /**
     * Added by Eason 
     * This method is used to update the character's health after receiving the damage from the enemy. 
    **/
    public int receiveDamage(BasicEnemy enemy, int damage) {
        
        if (wearableStrategy != null) {
            damage = wearableStrategy.defend(enemy, damage);
        }

        if (shieldStrategy != null) {
            damage = shieldStrategy.defend(enemy, damage);
        }

        // if the enenmy is tranced less than 5 rounds of fight, no damage to the player. 
        if (enemy.isTranced() && enemy.getTotalTranceTimes() <= 5) {
            damage = 0;
        } 

        // if the enemy is tranced more than 5 rounds of fight, the affected enemy would revert back to normal status.  
        if (enemy.getTotalTranceTimes() > 5) {
            enemy.setTranceStatus(false);
        }

        changeHealth(damage * -1);
        return damage;
    }

    /**
     * Added by Eason 
     * This is a basic setter method for attack strategy. 
     */
    public void setAttackStrategy(Weapon weapon) {
        this.attackStrategy = weapon.getAttack();
    }

    /**
     * A more basic setter method. Lets you put in the strategy directly
     * @param attackStrat
     */
    public void setAttackStrategy(AttackStrategy attackStrat) {
        this.attackStrategy = attackStrat;
    }

    /**
     * Added by Eason  
     * This method is used to attack the enemy.
     */
    public int attackEnemy(BasicEnemy enemy) {
        
        int attackDamage = getIntAttack();

        if (this.attackStrategy != null) {
            attackDamage += this.attackStrategy.attack(enemy); 
        }

        // Adds allies to the attack damage
        attackDamage += allies.get();
    
        // try to inflict trance to enemy
        tryToInflictTrance(enemy);
        if (enemy.isTranced() && enemy.getTotalTranceTimes() <= 5) {
            attackDamage += enemy.attack(System.currentTimeMillis()).getDamage();
        }

        //reduce 20% damage if player wears helemt
        if (wearableStrategy != null && wearableStrategy.toString().equals("helmet")) {
            attackDamage = attackDamage * 4 / 5;
        }

        enemy.loseHealth(attackDamage);
        return attackDamage;
    }

    public void tryToInflictTrance(BasicEnemy enemy) {
        Random rm = new Random();
        int chance = rm.nextInt(100);
        
        // staff has 25% to inflict trance to enemy. 
        if (attackStrategy != null && attackStrategy.toString().equals("staff") && chance < 25 && enemy.isTranced() == false) {
            enemy.setTranceStatus(true);
        }

        // add rounds of fight that the enemy is tranced.
        if (enemy.isTranced() == true) {
            enemy.addInflictTranceTimes();
        }
    }

}
