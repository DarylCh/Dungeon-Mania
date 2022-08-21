package dungeonmania.Entity.NonPlayerEntity.LiveEntities;

import dungeonmania.Entity.Player;
import dungeonmania.util.Position;

public interface PlayerStateSubscriber {
    public void notify(String potionState, String mindControlState, Position playerPosition, Position prevPosition, Player player);
}
