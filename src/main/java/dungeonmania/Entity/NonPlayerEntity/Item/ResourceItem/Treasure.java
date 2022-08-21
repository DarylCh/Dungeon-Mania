package dungeonmania.Entity.NonPlayerEntity.Item.ResourceItem;

import dungeonmania.util.Position;

public class Treasure extends ResourceItem {
    public Treasure(String id, String type, Position position, boolean isInteractable, int durability,
            boolean isCollectable, boolean isBuildable) {
        super(id, type, position, isInteractable, durability, isCollectable, isBuildable);
    }
}
