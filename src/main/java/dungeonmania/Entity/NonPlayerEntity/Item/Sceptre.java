package dungeonmania.Entity.NonPlayerEntity.Item;

import dungeonmania.util.Position;

public class Sceptre extends Item{
    int mindControlDuration;
    public Sceptre(String id, String type, Position position, boolean isInteractable, int durability,
            boolean isCollectable, boolean isBuildable, int mindControlDuration) {
        super(id, type, position, isInteractable, durability, isCollectable, isBuildable);
        this.mindControlDuration = mindControlDuration;
    }

}
