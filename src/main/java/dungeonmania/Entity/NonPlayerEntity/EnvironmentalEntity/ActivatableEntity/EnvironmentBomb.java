package dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.ActivatableEntity;

import java.util.Iterator;
import java.util.List;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.util.Position;
import dungeonmania.util.EntityHelper;

public class EnvironmentBomb extends ActivatableEntity {
    int bombRadius;

    public EnvironmentBomb(String id, String type, Position position, boolean isInteractable, int bombRadius) {
        super(id, type, position, isInteractable);
        this.bombRadius = bombRadius;
    }

    public int getBombRadius() {
        return this.bombRadius;
    }

    private void explode(List<NonPlayerEntity> entities) {
        Iterator itr = entities.iterator();
        while (itr.hasNext()) {
            NonPlayerEntity entity = (NonPlayerEntity) itr.next();
            if (((EntityHelper.withinRadius(this, entity, this.getBombRadius()))
                    && entity.getType() != "player")) {
                itr.remove();
            }

        }
        entities.remove(this);
    }

    public void activate(List<NonPlayerEntity> entities) {
        explode(entities);
    }

}
