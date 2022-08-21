package dungeonmania.Entity.NonPlayerEntity.Item.ResourceItem;

import dungeonmania.util.Position;

public class Key extends ResourceItem {
    private int keyId;

    public Key(String id, String type, Position position, boolean isInteractable, int durability, boolean isCollectable,
            boolean isBuildable, int keyId) {
        super(id, type, position, isInteractable, durability, isCollectable, isBuildable);
        this.keyId = keyId;
    }

    @Override
    public int getKeyId() {
        return this.keyId;
    }
}
