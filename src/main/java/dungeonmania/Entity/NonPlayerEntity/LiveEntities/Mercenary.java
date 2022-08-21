package dungeonmania.Entity.NonPlayerEntity.LiveEntities;
import dungeonmania.Entity.Player;
import dungeonmania.util.Position;
import java.util.*;

public class Mercenary extends LivePlayerAffectedEntity {
    private final List<String> blockableEntities = Arrays.asList("wall", "boulder", "zombie_toast_spawner", "door");
    private int allyAttack;
    private int allyDefence;
    private boolean bribedWithTreasure = false;
    public Mercenary(String id, String type, Position position, boolean isInteractable, int health, int baseDamage, int allyAttack, int allyDefence, int direction) {
        super(id, type, position, isInteractable, health, baseDamage, direction);
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public List<String> getBlockableEntities() {
        return blockableEntities;
    }

    public void notify(String potionState, String mindControlState, Position playerPosition, Position prevPosition, Player player) {
        setPlayerPosition(playerPosition);
        setPrevPosition(prevPosition);
        if (!(getState() instanceof AlliedState)) {
            switch (mindControlState) {
                case "normal":
                    switch (potionState) {
                        case "normal":
                            setState(new EnemyState(this));
                            break;
                        case "invisible":
                            setState(new ZombieState(this));
                            break;
                        case "invincible":
                            setState(new RunningState(this));
                            break;
                    }
                    break;
                case "mind_control":
                    setState(new AlliedState(this));
                    break;
            }
            
        } 
         
        else {
            if (mindControlState == "normal" && this.bribedWithTreasure == false) {
                setState(new EnemyState(this));
                this.setIsInteractable(true);
                this.toggleIsEnemy();
                player.removeAlly(this);
                     
            } else if (mindControlState == "mind_control") {
                setState(new AlliedState(this));
            }
            
        }
        
    }
    public void setBribedWithTreasure(boolean bribed) {
        this.bribedWithTreasure = bribed;
    }
    public boolean getBribedWithTreasure() {
        return this.bribedWithTreasure;
    }
    public int getAllyattack() {
        return allyAttack;
    }

    public int getAllyDefence() {
        return allyDefence;
    }
}