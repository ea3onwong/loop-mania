/**
 * This goal is for gold
 * z5309206
 */
package unsw.loopmania.Goals;

import unsw.loopmania.Character;

public class GoalsLeafGold implements GoalsComponent {

    private int requiredGold;

    public GoalsLeafGold(int requiredGold) {
        this.requiredGold = requiredGold;
    }

    @Override
    public boolean checkMeet(Character c) {
        if (c.getIntGold() >= requiredGold) {
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
        return "Total Gold: " + this.requiredGold + " ";
    }
    
}
