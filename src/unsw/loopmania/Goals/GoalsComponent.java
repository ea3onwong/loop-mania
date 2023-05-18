/**
 * Composite design pattern for all the goals
 * Used to determine how the player wins
 * Written by z5309206
 */
package unsw.loopmania.Goals;

import unsw.loopmania.Character;

public interface GoalsComponent {
    
    /**
     * Checks if the criteria has been met or not based on the character's stats
     * If not, returns false. If yes, returns true
     */
    public boolean checkMeet(Character c);

    /**
     * Adds subgoals
     * Only for branches such as AND / OR
     */
    public void addGoal(GoalsComponent goal);

    /**
     * Prints out the goals as a string.
     * This is normally used for debugging
     * @return
     */
    public String printOut();

}
