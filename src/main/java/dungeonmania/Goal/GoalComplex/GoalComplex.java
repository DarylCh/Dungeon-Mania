package dungeonmania.Goal.GoalComplex;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.Dungeon;
import dungeonmania.Goal.Goal;

public class GoalComplex implements Goal {

    public boolean goalAchieved;
    public List<Goal> listOfGoals = new ArrayList<Goal>();

    public String getGoalType() {
        return "GoalComplex";
    }

    public boolean getGoalAchieved() {
        return this.goalAchieved;
    }

    public void setGoalAchieved(Dungeon currentDungeon) {
        this.goalAchieved = false;
    }

    public void addToListOfGoals(Goal goal) {
        this.listOfGoals.add(goal);
    }

    public String returnGoalString() {
        return null;
    }

}