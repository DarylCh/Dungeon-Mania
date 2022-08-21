package dungeonmania.Entity.NonPlayerEntity.LiveEntities;

import java.util.List;

import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.util.Position;

public abstract class LivePlayerAffectedEntity extends LiveEntity implements PlayerStateSubscriber, LiveEntityState {
    private LiveEntityState entityState;
    private Position playerPosition;
    private Position prevPosition;

    public LivePlayerAffectedEntity(String id, String type, Position position, Boolean isInteractable, int health, int baseDamage, int direction) {
        super(id, type, position, isInteractable, health, baseDamage, direction);
    }

    public abstract void notify(String potionState, String mindControlState, Position playerPosition, Position prevPosition, Player player);
    
    public LiveEntityState getState() {
        return entityState;
    }

    public Position getPrevPosition(){
        return prevPosition;
    }

    public void setPrevPosition(Position position){
        this.prevPosition = position;
    }

    public void setState(LiveEntityState entityState) {
        this.entityState = entityState;       
    }

    public void setPlayerPosition(Position playerPosition) {
        this.playerPosition = playerPosition;
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public void moveNPC(List<NonPlayerEntity> entities) {
        getState().moveNPC(entities);
    }
}
