/**
 * Written by z5309206 Gordon Wang
 */
package unsw.loopmania.building;

import javafx.beans.property.SimpleIntegerProperty;

public class VampireCastleBuilding extends Building implements SpawnBuildingObserver {

    private boolean readyToSpawn;


    public VampireCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
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
        // Vampire castle spawns every 5 cycles
        if (cycle % 5 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
