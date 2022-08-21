package dungeonmania.Entity.NonPlayerEntity;

import dungeonmania.Dungeon;
import dungeonmania.Entity.Entity;
import dungeonmania.Entity.Player;
import dungeonmania.util.Position;

public abstract class NonPlayerEntity extends Entity {
    public NonPlayerEntity(String id, String type, Position position, boolean isInteractable) {
        super(id, type, position, isInteractable);
    }

    public void playerInteract(Player player, Dungeon dungeon, Position initialPosition) {
        return;
    }
}
