package dungeonmania.Entity.NonPlayerEntity.LiveEntities;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.Entity.EntitySubscriber;
import dungeonmania.Entity.Movable;
import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.util.Position;

public abstract class LiveEntity extends NonPlayerEntity implements Movable {
    private int health;
    private int cHp;
    private int direction;
    private int baseDamage;
    boolean isEnemy;
    EntitySubscriber subscriber;

    public LiveEntity(String id, String type, Position position, boolean isInteractable, int health, int baseDamage, int direction) {
        super(id, type, position, isInteractable);
        this.health = health;
        this.cHp = health;
        this.direction = direction;
        this.isEnemy = true;
        this.baseDamage = baseDamage;
    }

    public int getHp() {
        return cHp;
    }

    public void setHp(int hp) {
        if (hp <= 0) {
            // todo die
        }
        this.cHp = hp;
        if (this.cHp > this.health) {
            this.cHp = this.health;
        }
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    // public Position getPosition() {
    //     return position;
    // }

    public int getDirection() {
        return direction;
    }

    public boolean getIsEnemy(){
        return this.isEnemy;
    }

    public boolean toggleIsEnemy(){
        this.isEnemy = !this.isEnemy;
        return this.isEnemy;
    }

    // public List<String> getIgnorableStatics(){
    //     return this.ignorableStatics;
    // }
    
    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void informRemoval() {
        subscriber.informRemoval(this);
    }

    public abstract void moveNPC(List<NonPlayerEntity> entities);
    
    public void setSubscriber(EntitySubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void playerInteract(Player player, Dungeon dungeon, Position initialPosition) {
        if(!isEnemy){
            //player.setPosition(this.getPosition());
            //player.setPosition(new Position(10, 10, 2));
        } else if (!(player.getPotionState().equals("invisible"))) {
            player.initiateBattle(this, dungeon, initialPosition);
        }
        return;
    }
}
