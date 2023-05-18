/**
 * Written by Gordon Wang z5309206
 * Throw this error when the character does not have any more health left
 */

package unsw.loopmania.Exceptions;

public class CharacterIsDead extends Exception { 
    public CharacterIsDead(String errorMessage) {
        super(errorMessage);
    }
}
