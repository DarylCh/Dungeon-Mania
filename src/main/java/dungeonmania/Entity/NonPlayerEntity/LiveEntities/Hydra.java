package dungeonmania.Entity.NonPlayerEntity.LiveEntities;

import dungeonmania.Entity.Player;
import dungeonmania.util.Position;

public class Hydra extends LivePlayerAffectedEntity {
    private double healthIncreaseRate;
    private int healthIncreaseAmount;
    public Hydra(String id, String type, Position position, boolean isInteractable, int health, int baseDamage, int direction,
        double healthIncreaseRate, int healthIncreaseAmount){
        super(id, type, position, isInteractable, health, baseDamage, direction);
        this.healthIncreaseRate = healthIncreaseRate;
        this.healthIncreaseAmount = healthIncreaseAmount;
    }
    public void notify(String potionState, String mindControlState, Position playerPosition, Position prevPosition, Player player) {
        setPlayerPosition(playerPosition);
        switch (potionState) {
            case "normal":
                setState(new ZombieState(this));
                break;
            case "invisible":
                setState(new ZombieState(this));
                break;
            case "invincible":
                setState(new RunningState(this));
                break;
        }
    }

    public double getHealthIncreaseRate(){
        return this.healthIncreaseRate;
    }
    public int getHealthIncreaseAmount(){
        return this.healthIncreaseAmount;
    }
}
