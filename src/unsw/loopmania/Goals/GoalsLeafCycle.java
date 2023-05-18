/**
 * This goal is for cycle
 * z5309206
 */
package unsw.loopmania.Goals;

import unsw.loopmania.Character;

public class GoalsLeafCycle implements GoalsComponent {

    private int requiredCycle;

    public GoalsLeafCycle(int requiredCycle) {
        this.requiredCycle = requiredCycle;
    }

    @Override
    public boolean checkMeet(Character c) {
        if (c.getIntTotalCycles() >= requiredCycle) {
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
        return "Total Cycles: " + this.requiredCycle + " ";
    }
    
}
