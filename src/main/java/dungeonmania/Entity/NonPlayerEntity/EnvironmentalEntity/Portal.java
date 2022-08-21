package dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity;

import java.util.Arrays;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.util.Position;
import java.util.stream.Collectors;

public class Portal extends EnvironmentalEntity {
    private String colour;

    public Portal(String id, String type, Position position, boolean isInteractable, String colour) {
        super(id, type, position, isInteractable);
        this.colour = colour;
    }

    public String getColour() {
        return this.colour;
    }

    public Position findMatchingPortalPosition(List<NonPlayerEntity> entities) {

        List<Portal> portals = entities.stream()
                .filter(it -> it.getType().equals("portal")).map(Portal.class::cast)
                .collect(Collectors.toList());
        if (portals.size() == 1) {
            return null;
        }
        return portals.stream()
                .filter(it -> it.getColour().equals(this.colour) && it.getPosition() != this.getPosition())
                .findFirst().get().getPosition();
    }

    @Override
    public void playerInteract(Player player, Dungeon dungeon, Position initialPosition) {

        Position otherPortalPos = this.findMatchingPortalPosition(dungeon.getEntities());
        if (otherPortalPos == null) {
            return;
        }
        List<Position> cardinalPositions = otherPortalPos.getCardinallyAdjacentPositions();
        List<String> blockableEntities = Arrays.asList("wall", "boulder", "zombie_toast_spawner", "door");
        for (Position pos : cardinalPositions) {
            if (pos.isPositionReachable(blockableEntities, dungeon.getEntities())) {
                player.setPosition(pos);
                if (dungeon.isThereAPortalHere(pos) != null) {
                    dungeon.isThereAPortalHere(pos).playerInteract(player, dungeon, pos);

                }
                break;
            }

        }
        return;

    }

}
