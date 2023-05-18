/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;

public class ZombiePitBuilding extends Building implements SpawnBuildingObserver {

    private boolean readyToSpawn;

    public ZombiePitBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        readyToSpawn = false;
    }

    @Override
    public void setReadyToSpawn(boolean state) {
        this.readyToSpawn = state;
    }

    @Override
    public boolean isReadyToSpawn() {
        return this.readyToSpawn;
    }

    @Override
    public boolean doesCycleMeet(int cycle) {
        // Zombie pit always spawns every cycle
        return true;
    }
}