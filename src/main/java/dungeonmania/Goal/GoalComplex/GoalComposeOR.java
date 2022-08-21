package dungeonmania.Goal.GoalComplex;
import java.util.regex.Pattern;

import dungeonmania.Dungeon;
import dungeonmania.Goal.Goal;

public class GoalComposeOR extends GoalComplex {
    
    @Override
    public String getGoalType() {
        return "OR";
    }

    @Override
    public void setGoalAchieved(Dungeon currentDungeon) {
        Goal exitGoal = null;
        boolean atLeastOneGoalAchieved = false;
        boolean nonExitGoalAchieved = false;

        // figure out whether the goal has been achieved
        for (Goal goal : listOfGoals) {
            goal.setGoalAchieved(currentDungeon); // update goalAchieved

            if (goal.getGoalType().equals("exit")) {
                exitGoal = goal;
                // continue; // ignore exit goal for now
            } else {
                nonExitGoalAchieved = goal.getGoalAchieved();
            }

            if (goal.getGoalAchieved()) {
                atLeastOneGoalAchieved = true;
            }
        }

        if (exitGoal != null) {
            if (nonExitGoalAchieved) {
                this.goalAchieved = true;
                return;
            }

            // If the exit goal exists in conjunction
            // with another external goal which has yet to be achieved
            String goalString = currentDungeon.getGoalString();
            boolean match1 = Pattern.matches(".*AND \\(+:exit.*", goalString);
            boolean match2 = Pattern.matches(".*:exit\\)+\\sAND.*", goalString);
            //System.out.println("DEBUG OR: goalString="+goalString+", match1="+match1+", match2="+match2);
            if (match1 || match2) {
                this.goalAchieved = false;
                return;
            }
        } else {
            this.goalAchieved = atLeastOneGoalAchieved;
            return;
        }

        this.goalAchieved = atLeastOneGoalAchieved;
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
            String goalString = "(";

            for (Goal goal : this.listOfGoals) {
                goalString += goal.returnGoalString() + " OR ";
            }
            goalString = goalString.substring(0, goalString.length() - " OR ".length());
            
            goalString += ")";
            return goalString;
        }
    }
}