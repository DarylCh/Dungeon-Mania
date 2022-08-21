package dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.ActivatableEntity;

import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.EnvironmentalEntity;

import dungeonmania.util.Position;

import java.util.List;

public abstract class ActivatableEntity extends EnvironmentalEntity {
    public ActivatableEntity(String id, String type, Position position, Boolean isInteractable) {
        super(id, type, position, isInteractable);
    }

    public abstract void activate(List<NonPlayerEntity> entities);
}
