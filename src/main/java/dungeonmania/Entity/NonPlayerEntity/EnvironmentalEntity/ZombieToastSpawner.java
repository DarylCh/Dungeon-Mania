package dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends EnvironmentalEntity {
    public ZombieToastSpawner(String id, String type, Position position, Boolean isInteractable) {
        super(id, type, position, isInteractable);
    }

    public static List<Position> getAllZombieToastSpawnerPositions(List<NonPlayerEntity> entities) {
        return entities.stream()
                .filter(it -> it.getType().equals("zombie_toast_spawner")).map(it -> it.getPosition())
                .collect(Collectors.toList());

    }
}
