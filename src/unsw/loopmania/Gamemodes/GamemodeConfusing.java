package unsw.loopmania.Gamemodes;

/**
 * Confusing mode is the same as standard mode except it will randomise properties
 */
public class GamemodeConfusing extends GamemodeStandard {

    @Override
    public boolean randomiseProperties() {
        return true;
    }

}
