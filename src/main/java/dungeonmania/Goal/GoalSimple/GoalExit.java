package dungeonmania.Goal.GoalSimple;

import java.util.List;
import java.util.regex.Pattern;

import dungeonmania.Entity.*;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Dungeon;

public class GoalExit extends GoalSimple {
    
    @Override
    public String getGoalType() {
        return "exit";
    }

    @Override
    public void setGoalAchieved(Dungeon currentDungeon) {        
        // if the player is standing on the exit set goalAchieved to true
        Player player = currentDungeon.getPlayer();
        List<NonPlayerEntity> samePositionEntities = player.checkForOtherEntitiesOnSameCell(currentDungeon.getEntities());

        boolean playerIsStandingOnExit = samePositionEntities.stream().anyMatch((e) -> e.getType().equals("exit")); 

        String goalString = currentDungeon.getGoalString();
        boolean match1 = Pattern.matches(".*AND \\(+:exit.*", goalString);
        boolean match2 = Pattern.matches(".*:exit\\)+\\sAND.*", goalString);
        //System.out.println("DEBUG Exit: goalString="+goalString+", match1="+match1+", match2="+match2);
        if (match1 || match2) {
            this.goalAchieved = false;
            return;
        }

        this.goalAchieved = playerIsStandingOnExit;
    }

    @Override
    public String returnGoalString() {
        if (this.goalAchieved) {
            return "";
        } else {
            return ":exit";
        }
    }
}