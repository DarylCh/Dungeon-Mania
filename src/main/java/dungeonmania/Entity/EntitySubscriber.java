package dungeonmania.Entity;

import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;

public interface EntitySubscriber {
    public void informRemoval(NonPlayerEntity entity);
}
