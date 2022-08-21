package dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.util.Position;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.ActivatableEntity.ActivatableEntity;

public class FloorSwitch extends EnvironmentalEntity {
    private boolean isActive;
    private List<ActivatableEntity> subscribers = new ArrayList<>();

    public FloorSwitch(String id, String type, Position position, boolean isInteractable, boolean isActive) {
        super(id, type, position, isInteractable);
        this.isActive = isActive;
    }

    public void addSubscriber(ActivatableEntity entity) {
        subscribers.add(entity);
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void toggleIsActive() {
        this.isActive = !isActive;
    }

    public void activateEntities(List<NonPlayerEntity> entities) {

        for (ActivatableEntity entity : this.subscribers) {
            entity.activate(entities);
        }

    }

}
