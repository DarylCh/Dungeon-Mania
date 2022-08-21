package dungeonmania.Goal;
import dungeonmania.Dungeon;


public interface Goal {
    public String getGoalType();

    // Returns true if the goal has been achieved
    public boolean getGoalAchieved();

    // Sets whether the goal has been achieved
    public void setGoalAchieved(Dungeon currentDungeon);

    // Returns the goal represented in the string form
    public String returnGoalString();
}
