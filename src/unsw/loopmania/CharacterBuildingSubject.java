package unsw.loopmania;

import unsw.loopmania.building.SpawnBuildingObserver;

public interface CharacterBuildingSubject {
    
    public void registerBuilding(SpawnBuildingObserver observer);
    public void notifyBuilding();

}
