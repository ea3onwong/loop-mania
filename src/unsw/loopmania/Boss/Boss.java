package unsw.loopmania.Boss;


/**
 * Interface for all Boss Enemies of LoopManiaWorld
 * @author z5309206 Gordon Wang
 */
public interface Boss {
    /**
     * Changes how much the cyptoprice should be
     * @return the randomness of the new cypto price
     */
    public int affectTheCryptoPrices();

    /**
     * Drops total crypto
     * @return
     */
    public int dropCrypto();
}
