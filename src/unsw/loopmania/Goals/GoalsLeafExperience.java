/**
 * This goal is for experience
 * z5309206
 */
package unsw.loopmania.Goals;

import unsw.loopmania.Character;

public class GoalsLeafExperience implements GoalsComponent {

    private int requiredExp;

    public GoalsLeafExperience(int requiredExp) {
        this.requiredExp = requiredExp;
    }

    @Override
    public boolean checkMeet(Character c) {
        if (c.getPropertyExp().get() >= requiredExp) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void addGoal(GoalsComponent goal) {
        // Nothing to be added
        return;
    }

    @Override
    public String printOut() {
        return "Total Exp: " + this.requiredExp + " ";
    }

}