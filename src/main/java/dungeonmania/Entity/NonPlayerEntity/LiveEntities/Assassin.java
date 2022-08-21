package dungeonmania.Entity.NonPlayerEntity.LiveEntities;

import dungeonmania.Entity.Player;
import dungeonmania.util.Position;

public class Assassin extends Mercenary{
    private int bribeAmount;
    private double bribeFailRate;
    private int reconRadius;
    public Assassin(String id, String type, Position position, boolean isInteractable, 
        int health, int baseDamage, int allyAttack, int allyDefence, int direction, int bribeAmount, double bribeFailRate, int reconRadius){
        super(id, type, position, isInteractable, health, baseDamage, allyAttack, allyDefence, direction);
        this.bribeAmount = bribeAmount;
        this.bribeFailRate = bribeFailRate;
        this.reconRadius = reconRadius;

    }
    public int getReconRadius() {
        return this.reconRadius;
    }
    @Override
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
                            // check within recon radius or not
                            if (Math.abs(this.getPosition().getX() -playerPosition.getX()) > this.getReconRadius() || Math.abs(this.getPosition().getY()
                            - playerPosition.getY()) > this.getReconRadius()) {
                                setState(new ZombieState(this));
                            } else {
                                setState(new EnemyState(this));
                            }
                            
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
            if (mindControlState == "normal" && this.getBribedWithTreasure() == false) {
                setState(new EnemyState(this));
                    this.setIsInteractable(true);
                    this.toggleIsEnemy();
                    player.removeAlly(this);
                     
            } else if (mindControlState == "mind_control") {
                setState(new AlliedState(this));
            }
            
        }
    }
}
