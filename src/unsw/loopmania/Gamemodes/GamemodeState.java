/**
 * State design pattern for the gamemode 
 * Used to manage the gamemode the player chooses
 * Written by z5309206
 */
package unsw.loopmania.Gamemodes;

import unsw.loopmania.Character;
import unsw.loopmania.StaticEntity;

public interface GamemodeState {
    
    /**
     * Buys a sword. True if successful, false if not
     * @param character
     * @return boolean
     */
    public boolean buySword(Character character);

    /**
     * Buys a stake. True if successful, false if not
     * @param character
     * @return boolean
     */
    public boolean buyStake(Character character);

    /**
     * Buys a staff. True if successful, false if not
     * @param character
     * @return boolean
     */
    public boolean buyStaff(Character character);

    /**
     * Buys an armour. True if successful, false if not
     * @param character
     * @return boolean
     */
    public boolean buyArmour(Character character);

    /**
     * Buys a shield. True if successful, false if not
     * @param character
     * @return boolean
     */
    public boolean buyShield(Character character);

    /**
     * Buys a helmet. True if successful, false if not
     * @param character
     * @return boolean
     */
    public boolean buyHelmet(Character character);

    /**
     * Buys a potion. True if successful, false if not
     * @param character
     * @return boolean
     */
    public boolean buyPotion(Character character);

    /**
     * Buys an attack buff. True if successful, false if not
     * @param character
     * @return boolean
     */
    public boolean buyAttack(Character character);

    /**
     * Buys an max hp buff. True if successful, false if not
     * @param character
     * @return boolean
     */
    public boolean buyHealth(Character character);

    /**
     * Gets the price of item
     * applySell = true if want selling price
     *           = false if you want the original price
     */
    public int getPriceOfItem(StaticEntity item, boolean applySell);

    /**
     * Resets any data inside the state
     */
    public void resetPurchases();

    /**
     * Returns true if the gamemode is allowing for randomised properties
     * False if otherwise
     */
    public boolean randomiseProperties();

}
