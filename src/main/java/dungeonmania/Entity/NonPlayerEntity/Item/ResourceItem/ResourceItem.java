package dungeonmania.Entity.NonPlayerEntity.Item.ResourceItem;

import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.util.Position;

public abstract class ResourceItem extends Item {
    public ResourceItem(String id, String type, Position position, boolean isInteractable, int durability,
            boolean isCollectable, boolean isBuildable) {
        super(id, type, position, isInteractable, durability, isCollectable, isBuildable);
    }
}
