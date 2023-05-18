/**
 * Written by z5309206 Gordon Wang
 * For spawning buildings that need to observe the character's cycle change
 * as spawning buildings only spawn when a cycle is completed
 */

package unsw.loopmania.building;

public interface SpawnBuildingObserver {
    /**
     * Changes the building to be willing to spawn its enemies
     * @param state
     */
    public void setReadyToSpawn(boolean state);

    /**
     * Asks the building if its ready to spawn enemies
     * @return boolean
     */
    public boolean isReadyToSpawn();

    /**
     * Checks to see if the current cycle count meets the building's
     * criteria
     * @param cycle
     * @return boolean
     */
    public boolean doesCycleMeet(int cycle);
}
