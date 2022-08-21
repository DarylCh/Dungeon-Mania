package dungeonmania.Goal.GoalSimple;
import dungeonmania.Dungeon;
import dungeonmania.Goal.Goal;

public class GoalSimple implements Goal {
    public boolean goalAchieved;
    
    public boolean getGoalAchieved() {
        return this.goalAchieved;
    }

    public String getGoalType() {
        return "GoalSimple";
    }

    public void setGoalAchieved(Dungeon currentDungeon) {
        this.goalAchieved = false;
    }

    public String returnGoalString() {
        return "";
    }

}