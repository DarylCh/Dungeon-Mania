package dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity;

import dungeonmania.Dungeon;
import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.util.Position;
import java.util.List;
import java.util.stream.Collectors;

public class Boulder extends EnvironmentalEntity {
    public Boulder(String id, String type, Position position, boolean isInteractable, boolean isActive) {
        super(id, type, position, isInteractable);
    }

    @Override
    public void playerInteract(Player player, Dungeon dungeon, Position initialPosition) {
        Position movementDirection = Position.calculatePositionBetween(initialPosition, player.getPosition());
        Position otherSideOfBoulder = player.getPosition().translateBy(movementDirection);

        List<NonPlayerEntity> entitiesOnOtherSideOfBoulder = dungeon.getEntities().stream()
                .filter(ent -> ent.getPosition().equals(otherSideOfBoulder)).collect(Collectors.toList());
        for (NonPlayerEntity npe : entitiesOnOtherSideOfBoulder) {
            // If there is a wall or boulder on the other side of the boulder
            if (npe.getType().equals("wall") || npe.getType().equals("boulder")) {
                // Do not move the boulder
                // Move the player back to their initial position
                player.moveBackwards(initialPosition);
                return;
            } else if (npe.getType().equals("switch")) {
                FloorSwitch floorSwitch = (FloorSwitch) npe;
                floorSwitch.toggleIsActive();
                floorSwitch.activateEntities(dungeon.getEntities());
            }
        }

        // Otherwise, move the boulder in the direction the player moved
        // check if floor switch needs to be deactivated and deactivate
        List<FloorSwitch> floorswitches = dungeon.getEntities().stream()
                .filter(ent -> ent.getType().equals("switch") && ent.getPosition().equals(player.getPosition()))
                .map(FloorSwitch.class::cast).collect(Collectors.toList());
        floorswitches.forEach(e -> e.toggleIsActive());
        this.setPosition(otherSideOfBoulder);
    }
}