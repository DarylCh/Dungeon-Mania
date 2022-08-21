package dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity;

import dungeonmania.Dungeon;

import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.util.Position;

public class Door extends EnvironmentalEntity {
    private boolean isOpen = false;
    private int matchingKeyId;

    public Door(String id, String type, Position position, boolean isInteractable, boolean isActive, int key) {
        super(id, type, position, isInteractable);
        this.matchingKeyId = key;
    }

    public boolean checkKey(int keyId) {
        if (keyId == this.matchingKeyId) {
            return true;
        }
        return false;
    }

    public boolean isDoorOpen() {
        return this.isOpen;
    }

    public void setIsDoorOpen(boolean doorOpen) {
        this.isOpen = doorOpen;
    }

    @Override
    public void playerInteract(Player player, Dungeon dungeon, Position initialPosition) {
        // look for sunstone in inventory. if exists, use this to open door
        // and retain after use
        if (player.getInventory().searchInventory("sun_stone").size() == 1) {
            this.setIsDoorOpen(true);
        }
        // look for key in inventory
        else {
            Item key = null;
            if (player.getInventory().searchInventory("key").size() == 1) {
                key = player.getInventory().searchInventory("key").get(0);
            }
            
            if (key != null && this.checkKey(key.getKeyId())) {
                this.setIsDoorOpen(true);
                
                // remove key from inventory
                player.removeItemFromInventoryByType("key");
            } 
        }
        // if door is closed, player is pushed back onto original tile
        if (this.isDoorOpen() == false) {
            player.moveBackwards(initialPosition);
        }
        return;

    }

}
