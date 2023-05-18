/**
 * This goal is for the operator AND
 * z5309206
 */
package unsw.loopmania.Goals;

import java.util.ArrayList;
import unsw.loopmania.Character;

public class GoalsBranchAND implements GoalsComponent {

    // Keeps track of all the subgoals of this AND operator
    private ArrayList<GoalsComponent> listOfGoals = new ArrayList<GoalsComponent>();

    @Override
    public boolean checkMeet(Character c) {
        for (GoalsComponent g : listOfGoals) {
            // All goals must pass, since this is AND
            if (!g.checkMeet(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addGoal(GoalsComponent goal) {
        this.listOfGoals.add(goal);
    }

    @Override
    public String printOut() {
        String output = "";
        int totalGoalsLeft = this.listOfGoals.size();
        // Only adds an AND inbetween goals and not at the end
        for (GoalsComponent goal : this.listOfGoals) {
            output += goal.printOut();
            if (totalGoalsLeft > 1) {
                output += "AND ";
            }
            totalGoalsLeft--;
        }
        return output;
    }

}
