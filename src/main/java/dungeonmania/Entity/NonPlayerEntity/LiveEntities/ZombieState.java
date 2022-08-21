package dungeonmania.Entity.NonPlayerEntity.LiveEntities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.util.Position;

public class ZombieState implements LiveEntityState {
    LivePlayerAffectedEntity entity;
    ZombieState(LivePlayerAffectedEntity entity) {
        this.entity = entity;
    }

    public void moveNPC(List<NonPlayerEntity> entities) {
        Position pos = this.entity.getPosition();
        List<Position> cardinalPositions = pos.getCardinallyAdjacentPositions();
        List<String> blockableEntities = Arrays.asList("wall", "boulder", "zombie_toast_spawner", "door");
        Collections.shuffle(cardinalPositions);
        for (Position p : cardinalPositions) {
            if (p.isPositionReachable(blockableEntities, entities)) {
                this.entity.setPosition(p);
                break;
            }
        }
    }
}
