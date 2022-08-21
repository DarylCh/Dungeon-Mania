package dungeonmania.Goal.GoalSimple;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Dungeon;

public class GoalBoulders extends GoalSimple {
    
    @Override
    public String getGoalType() {
        return "boulders";
    }
    
    @Override
    public void setGoalAchieved(Dungeon currentDungeon) {
        // if each floorswitch has a boulder on it, return true
        List<NonPlayerEntity> listOfFloorSwitches = currentDungeon.getEntities().stream().filter(it -> it.getType().startsWith("switch")).collect(Collectors.toList());
        List<NonPlayerEntity> listOfBoulders = currentDungeon.getEntities().stream().filter(it -> it.getType().startsWith("boulder")).collect(Collectors.toList());

        int numFloorSwitchWithBoulder = 0;
        for (NonPlayerEntity floorSwitch : listOfFloorSwitches) {
            for (NonPlayerEntity boulder : listOfBoulders) {
                if (boulder.getPosition().equals(floorSwitch.getPosition())) {
                    numFloorSwitchWithBoulder++;
                }
            }
        }
        this.goalAchieved = numFloorSwitchWithBoulder == listOfFloorSwitches.size(); 
    }

    @Override
    public String returnGoalString() {
        if (this.goalAchieved) {
            return "";
        } else {
            return ":boulders";
        }
    }
}