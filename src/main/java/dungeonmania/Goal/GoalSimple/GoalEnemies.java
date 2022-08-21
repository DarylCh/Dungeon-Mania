package dungeonmania.Goal.GoalSimple;

import dungeonmania.Entity.*;
import dungeonmania.Config;
import dungeonmania.Dungeon;

public class GoalEnemies extends GoalSimple {
    
    @Override
    public String getGoalType() {
        return "enemies";
    }

    @Override
    public void setGoalAchieved(Dungeon currentDungeon) {
        // figure out whether the goal has been achieved
        Player player = currentDungeon.getPlayer();
        Config config = currentDungeon.getConfig();

        if (player.getEnemyKillCount() >= config.enemy_goal) {
            this.goalAchieved = true;
        } else {
            this.goalAchieved = false;
        }
    }

    @Override
    public String returnGoalString() {
        if (this.goalAchieved) {
            return "";
        } else {
            return ":enemies";
        }
    }
}