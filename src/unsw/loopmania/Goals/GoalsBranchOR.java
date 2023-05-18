/**
 * This goal is for the operator OR
 * z5309206
 */
package unsw.loopmania.Goals;

import java.util.ArrayList;
import unsw.loopmania.Character;

public class GoalsBranchOR implements GoalsComponent {

    // Keeps track of all the subgoals of this OR operator
    private ArrayList<GoalsComponent> listOfGoals = new ArrayList<GoalsComponent>();

    @Override
    public boolean checkMeet(Character c) {
        for (GoalsComponent g : listOfGoals) {
            // Not all goals must pass
            if (g.checkMeet(c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addGoal(GoalsComponent goal) {
        this.listOfGoals.add(goal);
    }

    @Override
    public String printOut() {
        String output = "";
        int totalGoalsLeft = this.listOfGoals.size();
        // Only adds an OR inbetween goals and not at the end
        for (GoalsComponent goal : this.listOfGoals) {
            output += goal.printOut();
            if (totalGoalsLeft > 1) {
                output += "OR ";
            }
            totalGoalsLeft--;
        }
        return output;
    }

}
