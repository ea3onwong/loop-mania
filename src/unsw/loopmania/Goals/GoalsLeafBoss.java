/**
 * This goal is for cycle
 * z5309206
 */
package unsw.loopmania.Goals;

import unsw.loopmania.Character;

public class GoalsLeafBoss implements GoalsComponent {


    public GoalsLeafBoss(int redundant) {
        // Boss leaf does not need an int
        return;
    }

    @Override
    public boolean checkMeet(Character c) {
        if (c.getIsDoggieSlain() && c.getIsElanSlain()) {
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
        return "Beat all 2 Boss";
    }
    
}
