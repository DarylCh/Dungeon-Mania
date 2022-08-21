package dungeonmania.Goal.GoalSimple;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.Inventory;

public class GoalTreasure extends GoalSimple {
    @Override
    public String getGoalType() {
        return "treasure";
    }

    @Override
    public void setGoalAchieved(Dungeon currentDungeon) {
        Config config = currentDungeon.getConfig();
        Inventory inventory = currentDungeon.retrieveInventory();
        int numSunstones = inventory.searchInventory("sun_stone").size();
        this.goalAchieved = (inventory.checkInventoryContainsNOfType(config.treasure_goal, "treasure") ||inventory.checkInventoryContainsNOfType(config.treasure_goal - numSunstones , "treasure") );
    }

    @Override
    public String returnGoalString() {
        if (this.goalAchieved) {
            return "";
        } else {
            return ":treasure";
        }
    }
}