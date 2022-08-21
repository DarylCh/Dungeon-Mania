package dungeonmania.Entity.NonPlayerEntity.LiveEntities;
import dungeonmania.Dungeon;
import dungeonmania.IdAssigner;
import dungeonmania.util.Position;
import java.util.*;
import dungeonmania.Entity.NonPlayerEntity.*;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.Boulder;

public class Spider extends LiveEntity {

    private List<Position> circuit = new ArrayList<Position>();

    public Spider(String id, String type, Position position, boolean isInteractable, int health, int baseDamage, int direction){
        super(id, type, position, isInteractable, health, baseDamage, direction);
    }

    @Override
    public void moveNPC(List<NonPlayerEntity> entities){
        Position position = this.getPosition();
        int x = position.getX();
        int y = position.getY();
        if(this.circuit.isEmpty()){
            
            this.circuit.add(new Position(x  , y-1));
            this.circuit.add(new Position(x+1, y-1));
            this.circuit.add(new Position(x+1, y));
            this.circuit.add(new Position(x+1, y+1));
            this.circuit.add(new Position(x  , y+1));
            this.circuit.add(new Position(x-1, y+1));
            this.circuit.add(new Position(x-1, y));
            this.circuit.add(new Position(x-1, y-1));
        }

        //check if next tile is bad
        int stuck = 0;
        for(int i= 0; i< entities.size(); i++){
            if(entities.get(i).getPosition().equals(circuit.get(0)) && entities.get(i) instanceof Boulder){
                Collections.reverse(circuit);
                i = 0;
                stuck = stuck + 1;
                if(stuck > 1){
                    break;
                }
                super.setPosition(this.circuit.get(0));
                this.circuit.add(this.circuit.get(0));
                this.circuit.remove(0);
            }
        }

        super.setPosition(this.circuit.get(0));
        this.circuit.add(this.circuit.get(0));
        this.circuit.remove(0);
    }

    public static void spawn(Dungeon currentDungeon) {
        List<String> blockableEntities = Arrays.asList("boulder");
            // assume spider spawns at an x position between 0 and 30
            // and a y position between 0 and 30
            Position pos = new Position(new Random().nextInt((31)), new Random().nextInt((31)));
            boolean spiderSuccess = false;
            while (!spiderSuccess) {
                if (pos.isPositionReachable(blockableEntities, currentDungeon.getEntities())) {
                    String id = IdAssigner.assignId();
                    Spider spider = new Spider(id, "spider", pos, false, currentDungeon.getConfig().spider_health, currentDungeon.getConfig().spider_attack, 1);
                    currentDungeon.addEntity(spider);
                    spider.setSubscriber(currentDungeon);
                    spiderSuccess = true;
                }
                pos = new Position(new Random().nextInt((31)), new Random().nextInt((31))); 
            }      
    }
}
