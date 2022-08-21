package dungeonmania.Entity.NonPlayerEntity.LiveEntities;

import java.util.List;
import java.util.*;

import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.util.Position;

public class RunningState implements LiveEntityState {
    private LivePlayerAffectedEntity entity;
    
    public RunningState(LivePlayerAffectedEntity entity) {
        this.entity = entity;
    }

    public void moveNPC(List<NonPlayerEntity> entities) {
        int playerX = this.entity.getPlayerPosition().getX();
        int playerY = this.entity.getPlayerPosition().getY();
        int x = this.entity.getPosition().getX();
        int y = this.entity.getPosition().getY();

        List<Position> circuit = new ArrayList<Position>();
        if(playerY >= y){
            circuit.add(new Position(x  , y-1));
        }
        if(playerX <= x){
            circuit.add(new Position(x+1, y));
        }
        if(playerY <= y){
            circuit.add(new Position(x  , y+1));
        }
        if(playerX >= x){
            circuit.add(new Position(x-1, y));
        }
        Collections.shuffle(circuit);
            
        List<String> blockableEntities = Arrays.asList("wall", "boulder", "zombie_toast_spawner", "door");
        for (Position p : circuit) {
           
            if (p.isPositionReachable(blockableEntities, entities)) {
                System.out.println(p);
                this.entity.setPosition(p);
                break;
            }
        }
            
            
        }
        
      
    
}
