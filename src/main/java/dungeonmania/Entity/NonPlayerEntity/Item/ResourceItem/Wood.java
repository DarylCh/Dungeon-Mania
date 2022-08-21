package dungeonmania.Entity.NonPlayerEntity.Item.ResourceItem;

import dungeonmania.util.Position;

public class Wood extends ResourceItem {
    public Wood(String id, String type, Position position, boolean isInteractable, int durability,
            boolean isCollectable, boolean isBuildable) {
        super(id, type, position, isInteractable, durability, isCollectable, isBuildable);
    }
}
