package dungeonmania.Entity.NonPlayerEntity.Item.Potion;

import java.util.List;

import dungeonmania.Config;
import dungeonmania.Inventory;
import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion {
    public InvisibilityPotion(String id, String type, Position position, boolean isInteractable, int durability,
            boolean isCollectable, boolean isBuildable, int potionDuration) {
        super(id, type, position, isInteractable, durability, isCollectable, isBuildable, potionDuration);
    }

    public void useItem(Inventory inventory, Player player, List<NonPlayerEntity> entities, Config c) {
        inventory.removeItemFromInventoryById(getId());
        
        if (player.getCurrentPotion() != null) {
            player.addToPotionQueue(this);
        } else {
            player.setCurrentPotion(this);
            player.setPotionState("invisible");
            player.setPotionTimer(getPotionDuration());
        }
    }

    public void playerConsume(Player player) {
        player.setPotionState("invisible");
        player.setPotionTimer(getPotionDuration());
        player.setCurrentPotion(this);
    }
}
