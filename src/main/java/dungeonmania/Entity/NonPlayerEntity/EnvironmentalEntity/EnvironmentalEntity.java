package dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity;

import dungeonmania.util.Position;
import dungeonmania.Entity.NonPlayerEntity.*;

public abstract class EnvironmentalEntity extends NonPlayerEntity {
    public EnvironmentalEntity(String id, String type, Position position, boolean isInteractable) {
        super(id, type, position, isInteractable);
    }

}
