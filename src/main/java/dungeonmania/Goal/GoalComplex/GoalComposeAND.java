package dungeonmania.Goal.GoalComplex;
import dungeonmania.Dungeon;
import dungeonmania.Goal.Goal;

import java.util.regex.Pattern;

public class GoalComposeAND extends GoalComplex {

    @Override
    public String getGoalType() {
        return "AND";
    }

    @Override
    public void setGoalAchieved(Dungeon currentDungeon) {
        boolean allNonExitGoalsAchieved = true; // assume true
        Goal exitGoal = null;

        for (Goal goal : listOfGoals) {

            if (goal.getGoalType().equals("exit")) {
                exitGoal = goal;
                continue; // ignore exit goal for now
            }

            goal.setGoalAchieved(currentDungeon);
            if (goal.getGoalAchieved() == false) {
                allNonExitGoalsAchieved = false;
            }
        }

        if (allNonExitGoalsAchieved) {
            if (exitGoal != null) {

                // If the exit goal exists in conjunction
                // with another external goal which has yet to be achieved
                String goalString = currentDungeon.getGoalString();
                boolean match1 = Pattern.matches(".*AND \\(+:exit.*", goalString);
                boolean match2 = Pattern.matches(".*:exit\\)+\\sAND.*", goalString);
                //System.out.println("DEBUG AND: goalString="+goalString+", match1="+match1+", match2="+match2);
                if (match1 || match2) {
                    this.goalAchieved = false;
                    return;
                }

                // Given that all the non-exit goals have been achieved
                // and an exit goal exists, check whether exit has been achieved
                exitGoal.setGoalAchieved(currentDungeon);
                this.goalAchieved = exitGoal.getGoalAchieved();
                return;
            }
        } 

        // if no exit goal exists
        this.goalAchieved = allNonExitGoalsAchieved;
    }
    
    @Override
    public boolean getGoalAchieved() {
        return this.goalAchieved;
    }

    @Override
    public String returnGoalString() {
        if (this.goalAchieved) {
            return "";
        } else {
            boolean atLeastOneGoalNotAchieved = false;
            String goalString = "(";
            for (Goal goal : this.listOfGoals) {
                if (goal.getGoalAchieved() == false) {
                    goalString += goal.returnGoalString() + " AND ";
                    atLeastOneGoalNotAchieved = true;
                }
            }
            if (atLeastOneGoalNotAchieved) {
                goalString = goalString.substring(0, goalString.length() - " AND ".length()) + ")";
            }
            return goalString;
        }
    }
}