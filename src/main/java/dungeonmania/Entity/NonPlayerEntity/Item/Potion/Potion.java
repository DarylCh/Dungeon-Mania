package dungeonmania.Entity.NonPlayerEntity.Item.Potion;

import java.util.List;

import dungeonmania.Config;
import dungeonmania.Inventory;
import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.Entity.NonPlayerEntity.Item.Usable;
import dungeonmania.util.Position;

public abstract class Potion extends Item implements Usable {
    private int potionDuration;
    public Potion(String id, String type, Position position, boolean isInteractable, int durability,
            boolean isCollectable, boolean isBuildable, int potionDuration) {
        super(id, type, position, isInteractable, durability, isCollectable, isBuildable);
        this.potionDuration = potionDuration;
    }

    public int getPotionDuration() {
        return potionDuration;
    }
    public abstract void useItem(Inventory inventory, Player player, List<NonPlayerEntity> entities, Config c);

    public abstract void playerConsume(Player player);
}