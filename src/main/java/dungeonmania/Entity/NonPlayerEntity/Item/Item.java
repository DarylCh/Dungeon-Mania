package dungeonmania.Entity.NonPlayerEntity.Item;

import dungeonmania.Dungeon;
import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.util.Position;

public abstract class Item extends NonPlayerEntity {
    private int durability;
    private boolean isCollectable;
    private boolean isBuildable;

    public Item(String id, String type, Position position, boolean isInteractable, int durability,
            boolean isCollectable, boolean isBuildable) {
        super(id, type, position, isInteractable);
        this.durability = durability;
        this.isCollectable = isCollectable;
        this.isBuildable = isBuildable;
    }

    public int getDurability() {
        return this.durability;
    }

    public void setDurability(int newDurability) {
        this.durability = newDurability;
    }

    public boolean getIsCollectable() {
        return this.isCollectable;
    }

    public boolean getIsBuildable() {
        return this.isBuildable;
    }

    @Override
    public void playerInteract(Player player, Dungeon dungeon, Position initialPosition) {
        // if key already exists in inventory, don't pick up another
        if (this.getType().equals("key") && player.getInventory().searchInventory("key").size() == 1) {
        }
        // move item from entity list to inventory
        else {
            dungeon.removeEntity(this);
            player.getInventory().addToInventory((Item) this);
        }
        return;

    }

    public int getKeyId() {
        return -1;
    }
}
