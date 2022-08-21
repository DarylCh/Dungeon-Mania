package dungeonmania.Entity.NonPlayerEntity.Item;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Config;
import dungeonmania.Inventory;
import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.FloorSwitch;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.ActivatableEntity.EnvironmentBomb;
import dungeonmania.util.EntityHelper;
import dungeonmania.util.Position;

public class Bomb extends Item implements Usable {
    int bombRadius;

    public Bomb(String id, String type, Position position, boolean isInteractable, int durability,
            boolean isCollectable, boolean isBuildable, int bombRadius) {
        super(id, type, position, isInteractable, durability, isCollectable, isBuildable);
        this.bombRadius = bombRadius;
    }

    public EnvironmentBomb placeEnvironmentBomb(String id, Position position, int bombRadius) {
        return new EnvironmentBomb(id, "bomb", position, false, bombRadius);
    }

    private int getBombRadius() {
        return this.bombRadius;
    }

    public void useItem(Inventory inventory, Player player, List<NonPlayerEntity> entities, Config c) {
        EnvironmentBomb bomb = this.placeEnvironmentBomb(this.getId(), player.getPosition(), this.getBombRadius());
        List<NonPlayerEntity> publishers = EntityHelper.findPublishers(player, entities);
        entities.add((NonPlayerEntity) bomb);
        player.getInventory().removeItemFromInventoryById(this.getId());
        if (publishers != null) {
            List<FloorSwitch> floorSwitches = publishers.stream().filter(e -> e.getType().equals("switch"))
                    .map(FloorSwitch.class::cast).collect(Collectors.toList());
            if (floorSwitches != null) {
                for (FloorSwitch floorSwitch : floorSwitches) {
                    if (floorSwitch.getIsActive()) {
                        bomb.activate(entities);
                    } else {
                        floorSwitch.addSubscriber(bomb);
                    }

                }
            }

        }
    }
}
