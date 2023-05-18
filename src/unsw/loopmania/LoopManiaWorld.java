package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import unsw.loopmania.building.*;
import unsw.loopmania.card.*;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.Boss.Boss;
import unsw.loopmania.Boss.Doggie;
import unsw.loopmania.Boss.ElanMuske;
import unsw.loopmania.Enemy.BasicEnemy;
import unsw.loopmania.Enemy.Slug;
import unsw.loopmania.Enemy.Thief;
import unsw.loopmania.Enemy.Vampire;
import unsw.loopmania.Enemy.Zombie;
import unsw.loopmania.Enemy.EnemyMoves.BasicMove;
import unsw.loopmania.Enemy.EnemyMoves.HealHit;
import unsw.loopmania.Enemy.EnemyMoves.StunHit;
import unsw.loopmania.Enemy.EnemyMoves.ThiefSwipe;
import unsw.loopmania.Enemy.EnemyMoves.ZombieCritHit;
import unsw.loopmania.Exceptions.CharacterIsDead;
import unsw.loopmania.Gamemodes.GamemodeState;
import unsw.loopmania.Goals.GoalsComponent;
import unsw.loopmania.Items.Anduril;
import unsw.loopmania.Items.Armour;
import unsw.loopmania.Items.Helmet;
import unsw.loopmania.Items.Potion;
import unsw.loopmania.Items.RareItemProperties;
import unsw.loopmania.Items.Ration;
import unsw.loopmania.Items.Shield;
import unsw.loopmania.Items.Staff;
import unsw.loopmania.Items.Stake;
import unsw.loopmania.Items.Sword;
import unsw.loopmania.Items.TheOneRing;
import unsw.loopmania.Items.TreeStump;
import unsw.loopmania.Items.Weapon;
import unsw.loopmania.Items.Wearable;

/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 */
public class LoopManiaWorld {

    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;
    /**
     * Added State. This manages the mode that the player has chosen
     */
    private GamemodeState modeState;

    /**
     * width of the world in GridPane cells
     */
    private int width;

    /**
     * height of the world in GridPane cells
     */
    private int height;

    /**
     * generic entitites - i.e. those which don't have dedicated fields
     */
    private List<Entity> nonSpecifiedEntities;

    private Character character;

    private List<BasicEnemy> enemies;

    private List<Card> cardEntities;

    private List<Card> removedCardEntities;

    private List<Entity> unequippedInventoryItems;

    private List<Building> buildingEntities;

    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse
     * them
     */
    private List<Pair<Integer, Integer>> orderedPath;

    private GoalsComponent allGoals;

    private ArrayList<BasicEnemy> queuedEnemies;

    private int totalQueuedZombies = 0;

    // Refers to how much a doggie coin can be sold for
    private SimpleIntegerProperty doggieCoinSellPrice;

    // Rare items property
    private RareItemProperties rareItemProperties;

    // Enabled Rare Items
    private boolean isRingEnabled;
    private boolean isAndurilEnabled;
    private boolean isStumpEnabled;

    /**
     * create the world (constructor)
     * 
     * @param width       width of world in number of cells
     * @param height      height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing
     *                    position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        this.width = width;
        this.height = height;
        rareItemProperties = new RareItemProperties();
        nonSpecifiedEntities = new ArrayList<>();
        queuedEnemies = new ArrayList<>();
        character = null;
        enemies = new ArrayList<>();
        cardEntities = new ArrayList<>();
        removedCardEntities = new ArrayList<>();
        unequippedInventoryItems = new ArrayList<>();
        this.orderedPath = orderedPath;
        buildingEntities = new ArrayList<>();
        doggieCoinSellPrice = new SimpleIntegerProperty(100);
        this.isRingEnabled = false;
        this.isAndurilEnabled = false;
        this.isStumpEnabled = false;

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * set the character. This is necessary because it is loaded as a special entity
     * out of the file
     * 
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Registers the building to the character
     * Buildings observe character
     * 
     * @param spawner
     */
    public void registerBuildingToCharacter(SpawnBuildingObserver spawner) {
        this.character.registerBuilding(spawner);
    }

    /**
     * Controller is observing the character
     */
    public void setController(LoopManiaWorldController lwc) {
        character.setController(lwc);
    }

    /**
     * ADDED CODE
     * Resets the world
     * 
     * @param seed
     */
    public void resetWorld(long seed) {

        // Resetting character and their walk path
        character.resetCharacter();

        // Resetting enemies
        for (BasicEnemy enemy : this.enemies) {
            enemy.destroy();
        }
        this.enemies.clear();
        // Resetting items
        for (Entity item : this.unequippedInventoryItems) {
            item.destroy();
        }
        this.unequippedInventoryItems.clear();
        // Resetting buildings
        ArrayList<Building> toBeDestroyed = new ArrayList<Building>();
        for (Building destroyedBuilding : this.buildingEntities) {
            if (!(destroyedBuilding instanceof HeroCastleBuilding)) {
                toBeDestroyed.add(destroyedBuilding);
            }
        }
        for (Building deleteBuilding : toBeDestroyed) {
            deleteBuilding.destroy();
            this.buildingEntities.remove(deleteBuilding);
        }
        // Resetting cards
        for (Card destroyedCard : this.cardEntities) {
            destroyedCard.destroy();
        }
        this.cardEntities.clear();

        // Resetting item properties
        if (this.modeState.randomiseProperties()) {
            this.rareItemProperties.randomiseProperties(seed);
        } else {
            this.rareItemProperties.resetProperties();
        }
    }

    /**
     * ADDED CODE
     * Changes the boolean state of the character's isDoggieSlain
     * 
     * @param state
     */
    public void setCharacterDoggieSlayState(boolean state) {
        character.setStateIsDoggieSlain(state);
    }

    /**
     * ADDED CODE
     * Changes the boolean state of the character's isElanSlain
     * 
     * @param state
     */
    public void setCharacterElanSlayState(boolean state) {
        character.setStateIsElanSlain(state);
    }

    /**
     * ADDED CODE
     * Sets the goal of the world via Strategy Design pattern
     * 
     * @param goal
     */
    public void setGoal(GoalsComponent goal) {
        this.allGoals = goal;
    }

    /**
     * ADDED CODE
     * Enable/disable rare items
     */
    public void enableRareItem(Entity item) {
        if (item instanceof TheOneRing) {
            this.isRingEnabled = true;
        } else if (item instanceof Anduril) {
            this.isAndurilEnabled = true;
        } else if (item instanceof TreeStump) {
            this.isStumpEnabled = true;
        }
    }

    /**
     * Returns a boolean depending on if the rare item is allowed.
     * Defaults to false if the item is not a rare item
     * 
     * @param item
     * @return boolean
     */
    public boolean isThisRareItemEnabled(Entity item) {
        if (item instanceof TheOneRing) {
            return this.isRingEnabled;
        } else if (item instanceof Anduril) {
            return this.isAndurilEnabled;
        } else if (item instanceof TreeStump) {
            return this.isStumpEnabled;
        }
        return false;
    }

    /**
     * ADDED CODE
     * Prints the goals that are set in this world
     * 
     * @return
     */
    public String printGoal() {
        return this.allGoals.printOut();
    }

    /**
     * ADDED CODE
     * Checks if the game world is ready to win
     * 
     * @return a boolean
     */
    public boolean isGoalMet() {

        if (this.allGoals == null) {
            return false;
        } else {
            return this.allGoals.checkMeet(character);
        }

    }

    /**
     * ADDED CODE
     * Gets the world's characters health property
     */
    public SimpleIntegerProperty getCharacterHealthProp() {
        return this.character.getPropertyHP();
    }

    public int getCharacterMaxHealth() {
        return this.character.getIntMaxHP();
    }

    /**
     * ADDED CODE
     * Changes the character's health
     * Use negative healthPoints to damage character
     * Positive healthPoints to heal
     * 
     * @param healthPoints
     */
    public void changeCharacterHealth(int healthPoints) {
        this.character.changeHealth(healthPoints);
    }

    /**
     * ADDED CODE
     * Changes the character's doggie coin count
     * Use negative gold to decrease gold
     * Positive gold to increase gold
     * 
     * @param gold
     */
    public void changeCharacterDoggieCoin(int coins) {
        this.character.changeDoggieCoin(coins);
    }

    /**
     * ADDED CODE
     * Gets the world's characters doggie coin property
     */
    public SimpleIntegerProperty getCharacterDoggieCoinProp() {
        return this.character.getPropertyDC();
    }

    /**
     * ADDED CODE
     * Gets the world's doggie coin sell price property
     * 
     * @param gold
     */
    public SimpleIntegerProperty getDoggieSellPriceProp() {
        return this.doggieCoinSellPrice;
    }

    /**
     * ADDED CODE
     * Change around the sell price of doggie coin
     * 
     * @param gold
     */
    public void randomiseDoggieSellPrice() {
        int upperBound = 200;
        int lowerBound = 100;
        // If Elan has been slain, then the upperbound and lowerbound shall change
        if (character.getIsElanSlain()) {
            upperBound = 50;
            lowerBound = 0;
            this.doggieCoinSellPrice.set(25);
        }
        Random rng = new Random(System.currentTimeMillis());
        // 1/3 chance of changing the sell price
        if (rng.nextInt(3) == 0) {

            this.doggieCoinSellPrice.set(rng.nextInt(upperBound - lowerBound) + lowerBound);

            int currentPrice = 0;
            for (BasicEnemy enemy : this.enemies) {
                if (enemy instanceof Boss) {
                    // Typecasting
                    Boss boss = (Boss) enemy;
                    currentPrice = this.doggieCoinSellPrice.get();
                    currentPrice += boss.affectTheCryptoPrices();
                    this.doggieCoinSellPrice.set(currentPrice);
                }
                // Changing Elan's attacks based on the price
                if (enemy instanceof ElanMuske) {
                    // Typecasting
                    ElanMuske elan = (ElanMuske) enemy;
                    elan.changeAttackDamage(this.doggieCoinSellPrice.get());
                }
            }

        }

    }

    /**
     * ADDED CODE
     * Changes the character's gold count
     * Use negative gold to decrease gold
     * Positive gold to increase gold
     * 
     * @param gold
     */
    public void changeCharacterGold(int gold) {
        this.character.changeGold(gold);
    }

    /**
     * ADDED CODE
     * Gets the world's characters gold property
     */
    public SimpleIntegerProperty getCharacterGoldProp() {
        return this.character.getPropertyGold();
    }

    /**
     * ADDED CODE
     * Gets the world's characters experience property
     */
    public SimpleIntegerProperty getCharacterExpProp() {
        return this.character.getPropertyExp();
    }

    /**
     * ADDED CODE
     * Changes the character's experience
     * Negative experience to remove experience
     * 
     * @param exp
     */
    public void changeCharacterExp(int exp) {
        this.character.changeExp(exp);
    }

    /**
     * ADDED CODE
     * Gets the world's character's ally property
     * 
     * @return SimpleIntegerProperty
     */
    public SimpleIntegerProperty getCharacterAlliesProp() {
        return this.character.getPropertyAlly();
    }

    /**
     * ADDED CODE
     * Gets the world's character's ally property
     * Negative number to remove allies
     */
    public void changeCharacterAllies(int totalRecruits) {
        this.character.changeAlly(totalRecruits);
    }

    /**
     * ADDED CODE
     * Adds a cycle to the character
     */
    public void addCharacterCycle() {
        this.character.increaseTotalCycles();
    }

    /**
     * Changes the gamemode of this world
     * 
     * @param gm
     */
    public void setGamemodeState(GamemodeState gm) {
        this.modeState = gm;
    }

    /**
     * ADDED CODE
     * Makes a purchase on the given item.
     * If the item can't be purchased, return false
     * otherwise, return true
     * 
     * @param itemName
     * @return boolean
     */
    public boolean makePurchase(String itemName) {
        switch (itemName) {
            case "sword":
                return this.modeState.buySword(this.character);
            case "stake":
                return this.modeState.buyStake(this.character);
            case "staff":
                return this.modeState.buyStaff(this.character);
            case "armour":
                return this.modeState.buyArmour(this.character);
            case "shield":
                return this.modeState.buyShield(this.character);
            case "helmet":
                return this.modeState.buyHelmet(this.character);
            case "potion":
                return this.modeState.buyPotion(this.character);
            case "attack":
                return this.modeState.buyAttack(this.character);
            case "health":
                return this.modeState.buyHealth(this.character);
            default:
                return false;
        }
    }

    /**
     * ADDED CODE
     * Sells one doggie coin if possible
     * 
     * @return true if sold successfully, false if otherwise
     */
    public boolean sellDoggieCoin() {
        if (character.getIntDoggieCoin() > 0) {
            character.changeGold(this.doggieCoinSellPrice.get());
            character.changeDoggieCoin(-1);
            return true;
        }
        return false;
    }

    /**
     * Gets the sell price of item
     * 
     * @param item
     * @return sell price
     */
    public int getSellPrice(StaticEntity item) {
        return this.modeState.getPriceOfItem(item, true);
    }

    /**
     * Gets the buy price of item
     * 
     * @param item
     * @return buy price
     */
    public int getBuyPrice(StaticEntity item) {
        return this.modeState.getPriceOfItem(item, false);
    }

    /**
     * Resets the number of times an item as been purchased
     */
    public void resetPurchases() {
        this.modeState.resetPurchases();
    }

    /**
     * add a generic entity (without it's own dedicated method for adding to the
     * world)
     * 
     * @param entity
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        nonSpecifiedEntities.add(entity);

    }

    /**
     * Adds a building
     * 
     * @param entity
     */
    public void addBuildingEntity(Building entity) {
        this.buildingEntities.add(entity);
    }

    /**
     * EDITED CODE
     * Spawns slimes in random position
     * spawns enemies if the conditions warrant it, adds to world
     * There can only be two slimes at once in the word
     * 
     * @return list of the enemies to be displayed on screen
     */
    public List<BasicEnemy> possiblySpawnEnemies() {

        Pair<Integer, Integer> pos = possiblyGetBasicSpawnPosition();
        List<BasicEnemy> spawningEnemies = new ArrayList<>();
        if (pos != null && (totalSlimeEnemies() < 2)) {
            int indexInPath = orderedPath.indexOf(pos);
            Slug enemy = new Slug(new PathPosition(indexInPath, orderedPath));
            enemies.add(enemy);
            spawningEnemies.add(enemy);
        }
        // ADDED CODE
        // Adds all the queued up enemies that have been added by other functions
        // and returns it to be spawned on UI
        if (this.queuedEnemies.size() > 0) {
            spawningEnemies.addAll(this.queuedEnemies);
            this.queuedEnemies.clear();
        }
        return spawningEnemies;
    }

    /**
     * ADDED CODE
     * Spawns a doggie on the path
     * Does not have a 100% success rate
     * 
     * @return Doggie
     */
    public Doggie spawnDoggie() {

        Pair<Integer, Integer> pos = possiblyGetBasicSpawnPosition();
        if (pos != null && totalDoggies() < 1) {
            int indexInPath = orderedPath.indexOf(pos);
            Doggie enemy = new Doggie(new PathPosition(indexInPath, orderedPath));
            enemies.add(enemy);
            return enemy;
        }
        return null;
    }

    /**
     * ADDED CODE
     * Spawns a thief on the path
     * A thief has roughly 2.5% chance of spawning every cycle if there aren't any
     * thieves
     * Thief can not spawn if player has less than 20 gold
     * 
     * @return Doggie
     */
    public Thief spawnThief() {
        int stolenGold = 0;
        Pair<Integer, Integer> pos = null;
        // Removes randomness from not getting position spawning
        while (pos == null) {
            pos = possiblyGetBasicSpawnPosition();
        }
        Random rng = new Random();
        if (pos != null && totalThief() < 1 && rng.nextInt(40) == 0 && this.character.getIntGold() >= 20) {
            // Stealing gold from player when spawning
            stolenGold = Math.floorDiv(this.character.getIntGold(), 2);
            this.character.changeGold(-stolenGold);
            int indexInPath = orderedPath.indexOf(pos);
            // Spawning and creating the thief
            Thief enemy = new Thief(new PathPosition(indexInPath, orderedPath), stolenGold);
            enemies.add(enemy);
            return enemy;
        }
        return null;
    }

    /**
     * ADDED CODE
     * Spawns a ElanMuske on the path
     * Has a 100% success rate
     * 
     * @return ElanMuske
     */
    public ElanMuske spawnElanMuske() {
        Pair<Integer, Integer> pos = null;
        while (pos == null) {
            pos = possiblyGetBasicSpawnPosition();
        }

        if (pos != null && !character.getIsElanSlain()) {
            int indexInPath = orderedPath.indexOf(pos);
            ElanMuske enemy = new ElanMuske(new PathPosition(indexInPath, orderedPath));
            enemies.add(enemy);
            return enemy;
        }

        return null;
    }

    /**
     * ADDED CODE
     * Randomly spawns a gold pile. There can only be one pile at a time
     * 
     * @return GoldPileBuilding
     */
    public GoldPileBuilding possiblySpawnGoldPiles() {
        Pair<Integer, Integer> pos = possiblyGetBasicSpawnPosition();
        if (pos != null && totalGoldPiles() < 1) {
            GoldPileBuilding pile = new GoldPileBuilding(new SimpleIntegerProperty(pos.getValue0()),
                    new SimpleIntegerProperty(pos.getValue1()));
            buildingEntities.add(pile);
            return pile;
        }
        return null;
    }

    /**
     * ADDED CODE
     * Randomly spawns a potion pile. There can only be one pile at a time
     * 
     * @return GoldPileBuilding
     */
    public PotionPileBuilding possiblySpawnPotionPile() {
        Pair<Integer, Integer> pos = possiblyGetBasicSpawnPosition();
        if (pos != null && totalPotionPiles() < 1) {
            PotionPileBuilding pile = new PotionPileBuilding(new SimpleIntegerProperty(pos.getValue0()),
                    new SimpleIntegerProperty(pos.getValue1()));
            buildingEntities.add(pile);
            return pile;
        }
        return null;
    }

    /**
     * ADDED CODE
     * Returns the total gold piles currently in the world
     * 
     * @return
     */
    private int totalGoldPiles() {
        int total = 0;
        for (Building building : this.buildingEntities) {
            if (building instanceof GoldPileBuilding) {
                total++;
            }
        }
        return total;
    }

    /**
     * ADDED CODE
     * Returns the total potion piles currently in the world
     * 
     * @return
     */
    private int totalPotionPiles() {
        int total = 0;
        for (Building building : this.buildingEntities) {
            if (building instanceof PotionPileBuilding) {
                total++;
            }
        }
        return total;
    }

    /**
     * ADDED CODE
     * Returns the total doggie in the world
     * 
     * @return int
     */
    private int totalDoggies() {
        int total = 0;
        for (BasicEnemy enemy : this.enemies) {
            if (enemy instanceof Doggie) {
                total++;
            }
        }
        return total;
    }

    /**
     * ADDED CODE
     * Returns the total thieves in the world
     * 
     * @return int
     */
    private int totalThief() {
        int total = 0;
        for (BasicEnemy enemy : this.enemies) {
            if (enemy instanceof Thief) {
                total++;
            }
        }
        return total;
    }

    /**
     * ADDED CODE
     * Spawn a zombie in the 3x3 area around x and y
     * 
     * @param x
     * @param y
     * @return BasicEnemy
     */
    public BasicEnemy spawnZombie(int x, int y) {
        Pair<Integer, Integer> pos = getSpawnPositionFromBuilding(x, y, System.currentTimeMillis());
        if (pos != null) {
            int indexInPath = orderedPath.indexOf(pos);

            Zombie zombie = new Zombie(new PathPosition(indexInPath, orderedPath));

            enemies.add(zombie);
            return zombie;
        }
        return null;
    }

    /**
     * ADDED CODE
     * Spawn a vampire in the 3x3 area around x and y
     * 
     * @param x
     * @param y
     * @return BasicEnemy
     */
    public BasicEnemy spawnVampire(int x, int y) {
        // Gets available spawning positions around x and y
        Pair<Integer, Integer> pos = getSpawnPositionFromBuilding(x, y, System.currentTimeMillis());
        if (pos != null) {
            int indexInPath = orderedPath.indexOf(pos);

            Vampire vampire = new Vampire(new PathPosition(indexInPath, orderedPath));

            enemies.add(vampire);
            return vampire;
        }
        return null;
    }

    /**
     * kill an enemy
     * 
     * @param enemy enemy to be killed
     */
    private void killEnemy(BasicEnemy enemy) {
        enemy.destroy();
        enemies.remove(enemy);
    }

    /**
     * EDITED CODE
     * run the expected battles in the world, based on current world state
     * 
     * @return list of enemies which have been killed
     */
    public List<BasicEnemy> runBattles(long seed) throws CharacterIsDead {
        List<BasicEnemy> defeatedEnemies = new ArrayList<BasicEnemy>();
        ArrayList<BasicEnemy> groupOfEnemies;
        int battleRadius = 0;
        for (BasicEnemy currEnemy : this.enemies) {

            // Pythagoras: a^2+b^2 < radius^2 to see if within radius

            // Checks if this enemy is within battle radius of the player
            battleRadius = currEnemy.getBattleRadius();
            if (calcDistance(character.getX(), currEnemy.getX(), character.getY(), currEnemy.getY()) < battleRadius) {
                // Initiates fight between character and the group of enemies
                groupOfEnemies = getSupportiveEnemies(currEnemy);
                if (groupFight(groupOfEnemies, seed) == false) {
                    throw new CharacterIsDead("The character does not have anymore health");
                } else {
                    defeatedEnemies.addAll(groupOfEnemies);
                }

            }
        }
        // Spawns in zombies around the player
        for (int zombie = 0; zombie < this.totalQueuedZombies; zombie++) {
            this.queuedEnemies.add(spawnZombie(character.getX(), character.getY()));
        }
        this.totalQueuedZombies = 0;
        for (BasicEnemy e : defeatedEnemies) {
            // IMPORTANT = we kill enemies here, because killEnemy removes the enemy from
            // the enemies list
            // if we killEnemy in prior loop, we get
            // java.util.ConcurrentModificationException
            // due to mutating list we're iterating over
            killEnemy(e);
        }
        return defeatedEnemies;
    }

    /**
     * ADDED CODE
     * Pits the character against the group of enemies
     * If the character wins, return true
     * Otherwise, returns false
     * 
     * @param groupOfEnemies
     * @return boolean
     */
    private boolean groupFight(ArrayList<BasicEnemy> groupOfEnemies, long seed) {
        for (BasicEnemy enemy : groupOfEnemies) {
            if (individualFight(enemy, seed, groupOfEnemies) == false) {
                return false;
            } else {
                // Adds the enemy's gold and exp to the character
                this.character.changeGold(enemy.dropGold());
                this.character.changeExp(enemy.dropExp());
            }
        }
        return true;
    }

    /**
     * ADDED CODE
     * Pits the character against an individual enemy
     * If character wins, return true
     * Otherwise, returns false
     * 
     * @param centralEnemy
     * @param seed
     * @param groupOfEnemies
     * @return boolean
     */
    public boolean individualFight(BasicEnemy enemy, long seed, ArrayList<BasicEnemy> groupOfEnemies) {
        BasicMove enemyMove;
        boolean skipMove = false;

        // Loops until one character is standing
        while (character.getIntHP() > 0 && enemy.getHealth() > 0) {

            if (!skipMove) {
                // character attacks enemy unless their move has been skipped
                character.attackEnemy(enemy);
            }
            skipMove = false;

            // Enemy attacks next
            enemyMove = enemy.attack(seed);
            if (enemyMove instanceof ZombieCritHit && character.getPropertyAlly().get() > 0) {
                // Spawns a zombie if the move was a critical zombie hit and the character
                // has some allies
                // Queues this enemy to be spawned in on the User Inferface
                this.totalQueuedZombies++;
                character.changeAlly(-1);
            } else if (enemyMove instanceof StunHit) {
                // Skips the character's next move
                skipMove = true;
            } else if (enemyMove instanceof HealHit && groupOfEnemies != null && groupOfEnemies.size() != 0) {
                // Healing supportive enemies
                for (BasicEnemy supportEnemy : groupOfEnemies) {
                    if (supportEnemy.getHealth() > 0 && !supportEnemy.equals(enemy)) {
                        // Heals enemy health if they still have HP
                        supportEnemy.loseHealth(-10);
                    }
                }
            } else if (enemyMove instanceof ThiefSwipe) {
                removeRandomItem();
            }

            // character receives damage from the enemy attack.
            character.receiveDamage(enemy, enemyMove.getDamage());

            if (character.getIntHP() == 0) {
                for (Entity e : unequippedInventoryItems) {

                    if (this.rareItemProperties.canThisRevivePlayer(e)) {
                        consumeRevivable(e.toString());
                        break;
                    }
                }
            }

        }

        // Determining the victor
        if (character.getIntHP() > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * ADDED CODE
     * For central enemy, find all the other enemies that the central enemy is
     * within
     * their support radius
     * 
     * @param centralEnemy
     * @return
     */
    public ArrayList<BasicEnemy> getSupportiveEnemies(BasicEnemy centralEnemy) {
        ArrayList<BasicEnemy> groupedEnemy = new ArrayList<BasicEnemy>();
        // Central enemy should be supportive of itself
        groupedEnemy.add(centralEnemy);

        int cenX = centralEnemy.getX(), cenY = centralEnemy.getY();
        int supportRadius = 0;

        for (BasicEnemy otherEnemy : this.enemies) {
            if (otherEnemy != centralEnemy) {
                supportRadius = otherEnemy.getSupportRadius();
                if (calcDistance(cenX, otherEnemy.getX(), cenY, otherEnemy.getY()) < supportRadius) {
                    groupedEnemy.add(otherEnemy);
                }
            }
        }

        return groupedEnemy;
    }

    /**
     * ADDED CODE
     * Calculates the pythagoras sqrt( a^2+b^2 ).
     * NOTE: Does not calculate if they are within each others radius, this
     * finds the UN-SQUARED distance between two points.
     * 
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return
     */
    private double calcDistance(int x1, int x2, int y1, int y2) {
        // Original equation given by starter code:
        // Math.pow((character.getX()-e.getX()), 2) +
        // Math.pow((character.getY()-e.getY()), 2)
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    /**
     * ADDED
     * run and performs all the buildings impact on character
     * run and performs all the buildings impact on enemies
     * 
     * @return list of enemies which have been killed
     */
    public void runBuildings() {
        int radius = 0;
        // Edited: need to remove buildings outside of iterating list to prevent
        // exceptions being thrown
        ArrayList<Building> destroyedBuildings = new ArrayList<Building>();

        // Buildings power over character
        for (Building building : buildingEntities) {
            // Pythagoras: a^2+b^2 < radius^2 to see if within radius
            radius = building.getRadius();
            if (calcDistance(character.getX(), building.getX(), character.getY(), building.getY()) < radius) {
                // get current HP of character
                int currentHP = character.getIntHP();

                building.performAbility(character);
                if (building instanceof GoldPileBuilding || building instanceof PotionPileBuilding) {
                    destroyedBuildings.add(building);
                }
                // If a campfire is next to the Field Kitchen
                // then the character can be healed by 5HP for each step the character walked in
                // the radius of campfire.
                if (building instanceof CampfireBuilding) {
                    for (Building buildings : buildingEntities) {
                        if (buildings instanceof FieldKitchenBuilding && calcDistance(building.getX(), buildings.getX(),
                                building.getY(), buildings.getY()) < 2) {
                            character.changeHealth(5);
                        }
                    }
                }
                // holy spring will disappear after being used
                if (building instanceof HolySpringBuilding && currentHP < character.getIntMaxHP()) {
                    destroyedBuildings.add(building);
                }
                // Alchemist's Tent and Survivors' Tents will disappear in the next loop, as
                // they are tents
                if (building instanceof HeroCastleBuilding) {
                    for (Building buildings : buildingEntities) {
                        if (buildings instanceof AlchemistTentBuilding || buildings instanceof SurvivorsTentsBuilding) {
                            destroyedBuildings.add(buildings);
                        }
                    }
                }
            }
        }

        // Destroying any buildings
        for (Building destroyed : destroyedBuildings) {
            destroyed.destroy();
            buildingEntities.remove(destroyed);
        }

        // Buildings power over enemies
        for (BasicEnemy e : enemies) {
            for (Building building : buildingEntities) {
                if (building == null) {
                    continue;
                }
                // Pythagoras: a^2+b^2 < radius^2 to see if within radius
                radius = building.getRadius();
                if (calcDistance(e.getX(), building.getX(), e.getY(), building.getY()) < radius) {

                    building.performAbility(e);
                    if (building instanceof TrapBuilding) {
                        destroyedBuildings.add(building);

                    }
                    if (e.getHealth() <= 0) {
                        killEnemy(e);
                    } else {

                    }
                }
            }
            // Destroying buildings before the next loop
            for (Building destroyed : destroyedBuildings) {
                destroyed.destroy();
                buildingEntities.remove(destroyed);
            }
        }
    }

    /**
     * check the Character walk into Field Kitchen or not
     * 
     * @return 1 if character pass through field kitchen, 2 if the field kitchen is
     *         next to village
     */
    public int checkCharacterStepOnFieldKitchen() {
        int radius = 0;
        for (Building building : buildingEntities) {
            // Pythagoras: a^2+b^2 < radius^2 to see if within radius
            radius = building.getRadius();
            if (calcDistance(character.getX(), building.getX(), character.getY(), building.getY()) < radius) {
                // check the building walked into is Field Kitchen or not
                if (building instanceof FieldKitchenBuilding) {
                    // check the field kitchen is next to village or not, return 2 if true
                    for (Building buildings : buildingEntities) {
                        if (buildings instanceof VillageBuilding && calcDistance(building.getX(), buildings.getX(),
                                building.getY(), buildings.getY()) < 2) {
                            return 2;
                        }
                    }
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * ADDED CODE
     * Runs all the buildings spawning enemies and then returns a list of the
     * enemies spawned
     * 
     * @return ArrayList<BasicEnemy>
     */
    public ArrayList<BasicEnemy> runSpawnBuildings() {
        ArrayList<BasicEnemy> spawnedEnemies = new ArrayList<BasicEnemy>();
        BasicEnemy entity = null;

        for (Building building : buildingEntities) {
            // Spawns in vampires and zombies for respective buildings
            if (building instanceof VampireCastleBuilding) {
                // Type casting
                VampireCastleBuilding spawner = (VampireCastleBuilding) building;
                if (spawner.isReadyToSpawn()) {
                    entity = spawnVampire(spawner.getX(), spawner.getY());
                    spawner.setReadyToSpawn(false);
                }
            } else if (building instanceof ZombiePitBuilding) {
                // Type casting
                ZombiePitBuilding spawner = (ZombiePitBuilding) building;
                if (spawner.isReadyToSpawn()) {
                    entity = spawnZombie(spawner.getX(), spawner.getY());
                    spawner.setReadyToSpawn(false);
                }
            }

            if (entity != null) {
                spawnedEnemies.add(entity);
            }
        }
        return spawnedEnemies;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public VampireCastleCard loadVampireCastleCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        VampireCastleCard vampireCastleCard = new VampireCastleCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(vampireCastleCard);
        return vampireCastleCard;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public ZombiePitCard loadZombiePitCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        ZombiePitCard zombiePitCard = new ZombiePitCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(zombiePitCard);
        return zombiePitCard;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public TowerCard loadTowerCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        TowerCard towerCard = new TowerCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(towerCard);
        return towerCard;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public VillageCard loadVillageCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        VillageCard villageCard = new VillageCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(villageCard);
        return villageCard;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public TrapCard loadTrapCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        TrapCard trapCard = new TrapCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        cardEntities.add(trapCard);
        return trapCard;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public CampfireCard loadCampfireCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        CampfireCard campfireCard = new CampfireCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(campfireCard);
        return campfireCard;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public BarracksCard loadBarracksCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        BarracksCard barracksCard = new BarracksCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(barracksCard);
        return barracksCard;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public AlchemistTentCard loadAlchemistTentCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        AlchemistTentCard alchemistTentCard = new AlchemistTentCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(alchemistTentCard);
        return alchemistTentCard;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public SurvivorsTentsCard loadSurvivorsTentsCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        SurvivorsTentsCard survivorsTentsCard = new SurvivorsTentsCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(survivorsTentsCard);
        return survivorsTentsCard;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public FieldKitchenCard loadFieldKitchenCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        FieldKitchenCard fieldKitchenCard = new FieldKitchenCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(fieldKitchenCard);
        return fieldKitchenCard;
    }

    /**
     * spawn a card in the world and return the card entity
     * 
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public HolySpringCard loadHolySpringCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }
        HolySpringCard holySpringCard = new HolySpringCard(new SimpleIntegerProperty(cardEntities.size()),
                new SimpleIntegerProperty(0));
        cardEntities.add(holySpringCard);
        return holySpringCard;
    }

    /**
     * remove card at a particular index of cards (position in gridpane of unplayed
     * cards)
     * 
     * @param index the index of the card, from 0 to length-1
     */
    private void removeCard(int index) {
        Card c = cardEntities.get(index);
        int x = c.getX();
        removedCardEntities.add(c);
        c.destroy();
        cardEntities.remove(index);
        shiftCardsDownFromXCoordinate(x);

        // drop exp, gold
        character.changeGold(c.dropGold());
        character.changeExp(c.dropExp());
    }

    /**
     * get the removed card entities
     * 
     * @return the removed card entities
     */

    public Card getRemovedCard() {
        if (removedCardEntities != null) {
            Card c = removedCardEntities.get(0);
            removedCardEntities.remove(0);
            return c;
        } else {
            return null;
        }

    }

    /**
     * get the card entities
     * 
     * @return the card entities
     */
    public List<Card> getCardEntities() {
        return cardEntities;
    }

    /**
     * get the building entities
     * 
     * @return the building entities
     */
    public List<Building> getBuildingEntities() {
        return buildingEntities;
    }

    /**
     * get the enemies entities
     * 
     * @return the enemies entities
     */
    public List<BasicEnemy> getEnemies() {
        return enemies;
    }

    /**
     * spawn a sword in the world and return the sword entity
     * 
     * @return a sword to be spawned in the controller as a JavaFX node
     */
    public Sword addUnequippedSword() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
            character.changeGold(5);
            character.changeExp(5);
        }

        // now we insert the new sword, as we know we have at least made a slot
        // available...
        Sword sword = new Sword(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(sword);
        return sword;
    }

    /**
     * Added by Eason
     * spawn a stake in the world and return the stake entity
     * 
     * @return a stake to be spawned in the controller as a JavaFx node
     */
    public Stake addUnequippedStake() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
            character.changeGold(7);
            character.changeExp(7);
        }

        // now we insert the new stake, as we know we have at least made a slot
        // available...
        Stake stake = new Stake(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(stake);
        return stake;
    }

    /**
     * Added by Eason
     * spawn a staff in the world and return the staff entity
     * 
     * @return a staff to be spawned in the controller as a JavaFx node
     */
    public Staff addUnequippedStaff() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
            character.changeGold(10);
            character.changeExp(10);
        }

        // now we insert the new staff, as we know we have at least made a slot
        // available...
        Staff staff = new Staff(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(staff);
        return staff;
    }

    /**
     * Added by Eason
     * spawn an armour in the world and return the armour entity
     * 
     * @return an armour to be spawned in the controller as a JavaFx node
     */
    public Armour addUnequippedArmour() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
            character.changeGold(10);
            character.changeExp(10);
        }

        // now we insert the new armour, as we know we have at least made a slot
        // available...
        Armour armour = new Armour(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(armour);
        return armour;
    }

    /**
     * Added by Eason
     * spawn a shield in the world and return the shield entity
     * 
     * @return a shield to be spawned in the controller as a JavaFx node
     */
    public Shield addUnequippedShield() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
            character.changeGold(7);
            character.changeExp(7);
        }

        // now we insert the new shield, as we know we have at least made a slot
        // available...
        Shield shield = new Shield(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(shield);
        return shield;
    }

    /**
     * Added by Eason
     * spawn a helmet in the world and return the helmet entity
     * 
     * @return a helmet to be spawned in the controller as a JavaFx node
     */
    public Helmet addUnequippedHelmet() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
            character.changeGold(5);
            character.changeExp(5);
        }

        // now we insert the new helmet, as we know we have at least made a slot
        // available...
        Helmet helmet = new Helmet(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(helmet);
        return helmet;
    }

    public Potion addUnequippedPotion() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            character.changeGold(5);
            character.changeExp(5);
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }

        // now we insert the new potion, as we know we have at least made a slot
        // available...
        Potion potion = new Potion(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(potion);
        return potion;
    }

    public Ration addUnequippedRation() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            character.changeGold(3);
            character.changeExp(3);
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }

        // now we insert the new ration, as we know we have at least made a slot
        // available...
        Ration ration = new Ration(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(ration);
        return ration;
    }

    public TheOneRing addUnequippedTheOneRing() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            character.changeGold(50);
            character.changeExp(50);
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }

        // now we insert the new the one ring, as we know we have at least made a slot
        // available...
        TheOneRing ring = new TheOneRing(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(ring);
        return ring;
    }

    public Anduril addUnequippedAnduril() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            character.changeGold(250);
            character.changeExp(250);
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }

        // now we insert the new potion, as we know we have at least made a slot
        // available...
        Anduril anduril = new Anduril(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(anduril);
        return anduril;
    }

    public TreeStump addUnequippedTreeStump() {

        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        if (firstAvailableSlot == null) {
            // eject the oldest unequipped item and replace it... oldest item is that at
            // beginning of items
            character.changeGold(250);
            character.changeExp(250);
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
        }

        // now we insert the new potion, as we know we have at least made a slot
        // available...
        TreeStump treeStump = new TreeStump(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
                new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        unequippedInventoryItems.add(treeStump);
        return treeStump;
    }

    /**
     * remove an item by x,y coordinates
     * 
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y) {
        Entity item = getUnequippedInventoryItemEntityByCoordinates(x, y);
        removeUnequippedInventoryItem(item);
    }

    /**
     * run moves which occur with every tick without needing to spawn anything
     * immediately
     */
    public void runTickMoves() {
        character.moveDownPath();
        moveBasicEnemies();
    }

    /**
     * ADDED CODE
     * Removes the first item that matches the itemName given from the player's
     * unequipped inventory.
     * 
     * @param itemName
     * @return true if item removed, false if not
     */
    public boolean removeUnequippedInventoryItemByClassName(String itemName) {
        for (Entity e : unequippedInventoryItems) {
            if (e.toString().equals(itemName.toLowerCase())) {
                removeUnequippedInventoryItem(e);
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player's inventory is full or not
     * 
     * @return true if full, false if not
     */
    public boolean isInventoryFull() {
        int capacity = unequippedInventoryHeight * unequippedInventoryWidth;
        capacity -= unequippedInventoryItems.size();

        if (capacity > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * remove an item from the unequipped inventory
     * 
     * @param item item to be removed
     */
    private void removeUnequippedInventoryItem(Entity item) {
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates
     * assumes that no 2 unequipped inventory items share x and y coordinates
     * 
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    private Entity getUnequippedInventoryItemEntityByCoordinates(int x, int y) {
        for (Entity e : unequippedInventoryItems) {
            if ((e.getX() == x) && (e.getY() == y)) {
                return e;
            }
        }
        return null;
    }

    /**
     * remove item at a particular index in the unequipped inventory items list
     * (this is ordered based on age in the starter code)
     * 
     * @param index index from 0 to length-1
     */
    private void removeItemByPositionInUnequippedInventoryItems(int index) {
        Entity item = unequippedInventoryItems.get(index);
        item.destroy();
        unequippedInventoryItems.remove(index);
    }

    /**
     * ADDED CODE
     * Removes an item randomly. Does not remove anything if the player has no items
     */
    private void removeRandomItem() {
        Random rng = new Random();
        int totalItems = this.unequippedInventoryItems.size();
        if (totalItems != 0) {
            removeItemByPositionInUnequippedInventoryItems(rng.nextInt(totalItems));
        }

    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the
     * unequipped inventory
     * 
     * @return x,y coordinate pair
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForItem() {
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available
        // slot defined by looking row by row
        for (int y = 0; y < unequippedInventoryHeight; y++) {
            for (int x = 0; x < unequippedInventoryWidth; x++) {
                if (getUnequippedInventoryItemEntityByCoordinates(x, y) == null) {
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * shift card coordinates down starting from x coordinate
     * 
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftCardsDownFromXCoordinate(int x) {
        for (Card c : cardEntities) {
            if (c.getX() >= x) {
                c.x().set(c.getX() - 1);
            }
        }
    }

    /**
     * EDITED CODE
     * move all enemies
     * Vampires will try and avoid campfires
     */
    private void moveBasicEnemies() {

        for (BasicEnemy e : enemies) {
            e.move(-1);
            if (e instanceof Vampire) {
                avoidCampfire(e);
            }
        }

    }

    /**
     * With the given enemy, move them the other way from a campfire
     * 
     * @param e
     */
    private void avoidCampfire(BasicEnemy e) {
        Random rng = new Random();
        double distance = 0;
        for (Building building : this.buildingEntities) {
            if (building instanceof CampfireBuilding) {
                distance = calcDistance(e.getX(), building.getX(), e.getY(), building.getY());
                while (distance < building.getRadius()) {
                    if (rng.nextInt(2) == 1) {
                        e.moveUpPath();
                    } else {
                        e.moveDownPath();
                    }
                    distance = calcDistance(e.getX(), building.getX(), e.getY(), building.getY());
                }
            }
        }
    }

    /**
     * get a randomly generated position which could be used to spawn an enemy
     * 
     * @return null if random choice is that wont be spawning an enemy or it isn't
     *         possible, or random coordinate pair if should go ahead
     */
    private Pair<Integer, Integer> possiblyGetBasicSpawnPosition() {

        // has a chance spawning a basic enemy on a tile the character isn't on or
        // immediately before or after (currently space required = 2)...
        Random rand = new Random();
        // Slug has a 10% chance of spawning with each tick
        int choice = rand.nextInt(10);

        if (choice == 0) {
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + orderedPath.size()) % orderedPath.size();
            int endNotAllowed = (indexPosition + 3) % orderedPath.size();
            // note terminating condition has to be != rather than < since wrap around...
            for (int i = endNotAllowed; i != startNotAllowed; i = (i + 1) % orderedPath.size()) {
                orderedPathSpawnCandidates.add(orderedPath.get(i));
            }

            // choose random choice
            Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates
                    .get(rand.nextInt(orderedPathSpawnCandidates.size()));

            return spawnPosition;
        }
        return null;
    }

    /**
     * Finds the total slime enemies currently in the world
     * 
     * @return
     */
    private int totalSlimeEnemies() {
        int total = 0;
        for (BasicEnemy e : this.enemies) {
            if (e instanceof Slug) {
                total++;
            }
        }
        return total;
    }

    /**
     * ADDED CODE
     * Finds a path that is in the 3 x 3 around the building's x and y
     * 
     * @param x
     * @param y
     * @param seed
     * @return Pair<Integer, Integer>
     */
    private Pair<Integer, Integer> getSpawnPositionFromBuilding(int x, int y, long seed) {
        int posX = 0, posY = 0;
        List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
        for (Pair<Integer, Integer> position : orderedPath) {
            posX = position.getValue0();
            posY = position.getValue1();
            if (posX >= x - 1 && posX <= x + 1) {
                if (posY >= y - 1 && posY <= y + 1) {
                    orderedPathSpawnCandidates.add(position);
                }
            }
        }
        Random rand = new Random(seed);

        // Choosing one of the random path tiles to spawn on
        Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates
                .get(rand.nextInt(orderedPathSpawnCandidates.size()));
        return spawnPosition;
    }

    /**
     * remove a card by its x, y coordinates, vampire castle will only be on
     * non-path tiles adjacent to the path
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public VampireCastleBuilding convertVampireCastleCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (!checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            VampireCastleBuilding newBuilding = new VampireCastleBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);
            character.registerBuilding(newBuilding);
            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }
    }

    /**
     * remove a card by its x, y coordinates, zombie pit will only be on non-path
     * tiles adjacent to the path
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public ZombiePitBuilding convertZombiePitCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (!checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            ZombiePitBuilding newBuilding = new ZombiePitBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);
            character.registerBuilding(newBuilding);
            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }
    }

    /**
     * remove a card by its x, y coordinates, tower will only be on non-path tiles
     * adjacent to the path
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public TowerBuilding convertTowerCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (!checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            TowerBuilding newBuilding = new TowerBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }
    }

    /**
     * remove a card by its x, y coordinates, village will only be on path tiles
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public VillageBuilding convertVillageCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            VillageBuilding newBuilding = new VillageBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }
    }

    /**
     * remove a card by its x, y coordinates, Trap will only be on path tiles
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public TrapBuilding convertTrapCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            TrapBuilding newBuilding = new TrapBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }

    }

    /**
     * remove a card by its x, y coordinates, Campfire will be on any non-path tile
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public CampfireBuilding convertCampfireCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (!checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            CampfireBuilding newBuilding = new CampfireBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }
    }

    /**
     * remove a card by its x, y coordinates, barracks only be on path tiles
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public BarracksBuilding convertBarracksCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX,
            int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            BarracksBuilding newBuilding = new BarracksBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }
    }

    /**
     * remove a card by its x, y coordinates, Alchemist's Tent only be on path tiles
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public AlchemistTentBuilding convertAlchemistTentCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            AlchemistTentBuilding newBuilding = new AlchemistTentBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }
    }

    /**
     * remove a card by its x, y coordinates, Survivors' Tents only be on path tiles
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public SurvivorsTentsBuilding convertSurvivorsTentsCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            SurvivorsTentsBuilding newBuilding = new SurvivorsTentsBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }
    }

    /**
     * remove a card by its x, y coordinates, Field Kitchen only be on path tiles
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public FieldKitchenBuilding convertFieldKitchenCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            FieldKitchenBuilding newBuilding = new FieldKitchenBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }
    }

    /**
     * remove a card by its x, y coordinates, Holy Spring only be on path tiles
     * 
     * @param cardNodeX     x index from 0 to width-1 of card to be removed
     * @param cardNodeY     y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public HolySpringBuilding convertHolySpringCardToBuildingByCoordinates(int cardNodeX, int cardNodeY,
            int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c : cardEntities) {
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)) {
                card = c;
                break;
            }
        }

        if (checkPathTileOrNonPathTile(buildingNodeX, buildingNodeY)) {
            // now spawn building
            HolySpringBuilding newBuilding = new HolySpringBuilding(new SimpleIntegerProperty(buildingNodeX),
                    new SimpleIntegerProperty(buildingNodeY));
            buildingEntities.add(newBuilding);

            // destroy the card
            card.destroy();
            cardEntities.remove(card);
            shiftCardsDownFromXCoordinate(cardNodeX);

            return newBuilding;
        } else {

            card.destroy();
            cardEntities.remove(card);
            return null;
        }
    }

    /**
     * 
     * @param X x index from 0 to width-1 of building to be added
     * @param Y y index from 0 to height-1 of building to be added
     * @return true if target position is PathTile, return false if target position
     *         is non-PathTile
     */
    public boolean checkPathTileOrNonPathTile(int X, int Y) {
        Pair<Integer, Integer> targetPosition = new Pair<Integer, Integer>(X, Y);
        if (orderedPath.contains(targetPosition)) {
            return true;
        } else {
            return false;
        }

    }

    // Added by Eason
    public void addBackendArmour(int x, int y) {
        Wearable armour = new Armour(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
        character.setDefendStrategy(armour);
    }

    // Added by Eason
    public void addBackendShield(int x, int y) {
        Wearable shield = new Shield(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
        character.setDefendStrategy(shield);
    }

    // Added by Eason
    public void addBackendHelmet(int x, int y) {
        Wearable helmet = new Helmet(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
        character.setDefendStrategy(helmet);
    }

    // Added by Eason
    public void addBackendSword(int x, int y) {
        Weapon sword = new Sword(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
        character.setAttackStrategy(sword);
    }

    // Added by Eason
    public void addBackendStake(int x, int y) {
        Weapon stake = new Stake(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
        character.setAttackStrategy(stake);
    }

    // Added by Eason
    public void addBackendStaff(int x, int y) {
        Weapon staff = new Staff(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
        character.setAttackStrategy(staff);
    }

    // Added by Adam
    public void addBackendAnduril(int x, int y) {
        Weapon anduril = new Anduril(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
        character.setAttackStrategy(anduril);
        if (this.modeState.randomiseProperties()) {
            character.setDefendStrategy(this.rareItemProperties.getTreeStumpDefend());
        }
    }

    // Added by Adam
    public void addBackendTreeStump(int x, int y) {
        TreeStump treeStump = new TreeStump(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
        character.setDefendStrategy(treeStump);
        if (this.modeState.randomiseProperties()) {
            character.setAttackStrategy(this.rareItemProperties.getTreeStumpAttack());
        }
    }

    // Added by Adam
    public void buyAttackIncrease() {
        character.changeAttack(2);
    }

    // Added by Adam
    public void buyMaxHPIncrease() {
        character.changeMaxHealth(5);
    }

    // Added by Adam
    public boolean consumePotion() {
        if (removeUnequippedInventoryItemByClassName("potion")) {
            character.changeHealth(50);
            return true;
        }
        return false;
    }

    // Added by Dongfei
    public void consumeRation() {
        if (removeUnequippedInventoryItemByClassName("ration")) {
            character.changeHealth(30);
        }
    }

    // Added by Adam
    public void consumeRevivable(String className) {
        removeUnequippedInventoryItemByClassName(className);
        character.changeHealth(99999);
    }
}