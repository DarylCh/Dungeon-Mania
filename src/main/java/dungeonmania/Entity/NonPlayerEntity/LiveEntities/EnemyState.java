package dungeonmania.Entity.NonPlayerEntity.LiveEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.Door;
import dungeonmania.util.Path;
import dungeonmania.util.Position;

public class EnemyState implements LiveEntityState {
    private Mercenary mercenary;
    EnemyState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }
    
    public void moveNPC(List<NonPlayerEntity> entities) {
        Path shortestPathToPlayer = findShortestPath(entities, mercenary.getPlayerPosition());
        while (!(shortestPathToPlayer.findPreviousPosition().equals(mercenary.getPosition()))) {
            shortestPathToPlayer = shortestPathToPlayer.getPrev();
        }
        mercenary.setPosition(shortestPathToPlayer.getPosition());
    }

    private Path findShortestPath(List<NonPlayerEntity> entities, Position playerPosition) throws IllegalArgumentException {
        List<Position> visited = new ArrayList<Position>();
        List<Path> queue = new ArrayList<Path>();
        Path original = new Path(mercenary.getPosition(), null);
        queue.add(original);
        visited.add(original.getPosition());
        while (queue.size() > 0) {
            Path pos = queue.remove(0);
            if (pos.getPosition().equals(playerPosition)) {
                return pos;
            }
            for (Position newPos : pos.getPosition().getCardinallyAdjacentPositions()) {
                if (positionIsNew(visited, newPos)) {
                    List<NonPlayerEntity> blockingEntities = entities.stream().filter(ent -> ent.getPosition().equals(newPos) && 
                        mercenary.getBlockableEntities().contains(ent.getType())).collect(Collectors.toList());

                    for (NonPlayerEntity entity : blockingEntities) {
                        if (entity instanceof Door) {
                            Door door = (Door) entity;
                            if (door.isDoorOpen()) {
                                blockingEntities.remove(door);
                            }
                        }
                    }
                
                    if (blockingEntities.size() == 0) {
                        Path newPath = new Path(newPos, pos);
                        queue.add(newPath);
                        visited.add(newPath.getPosition());
                    }

                }
            }
        }
        Path stayStill = new Path(mercenary.getPosition(), null);
        Path stayStill2 = new Path(mercenary.getPosition(), stayStill);
        return stayStill2;
        //throw new IllegalArgumentException("no valid route to the player found");
    }

    private Boolean positionIsNew(List<Position> visited, Position newPosition) {
        List<Position> duplicatePositions = visited.stream().filter(pos -> pos.equals(newPosition)).collect(Collectors.toList());
        return duplicatePositions.size() == 0 ? true : false;
    }


}
