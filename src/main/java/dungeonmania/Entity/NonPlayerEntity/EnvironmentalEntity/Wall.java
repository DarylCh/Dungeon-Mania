package dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity;

import dungeonmania.Dungeon;
import dungeonmania.Entity.Player;
import dungeonmania.util.Position;

public class Wall extends EnvironmentalEntity {
    public Wall(String id, String type, Position position, boolean isInteractable) {
        super(id, type, position, isInteractable);
    }

    @Override
    public void playerInteract(Player player, Dungeon dungeon, Position initialPosition) {
        // wall should push player back onto previous tile
        player.moveBackwards(initialPosition);
        return;

    }
}
