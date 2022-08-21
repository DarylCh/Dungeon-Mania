package dungeonmania.Entity;

import java.util.*;

import dungeonmania.Config;
import dungeonmania.Dungeon;
import dungeonmania.Inventory;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.Entity.NonPlayerEntity.Item.Usable;
import dungeonmania.Entity.NonPlayerEntity.Item.Potion.Potion;
import dungeonmania.Entity.NonPlayerEntity.LiveEntities.LiveEntity;
import dungeonmania.Entity.NonPlayerEntity.LiveEntities.Mercenary;
import dungeonmania.Entity.NonPlayerEntity.LiveEntities.PlayerStateSubscriber;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.Battle.Battle;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.Iterator;
import java.util.stream.Collectors;
import dungeonmania.Entity.NonPlayerEntity.Item.BattleItem.BattleItem;

public class Player extends Entity {
    private Inventory inventory;
    private List<String> buildables = new ArrayList<String>();
    private List<Potion> potionQueue = new ArrayList<Potion>();
    private double health;
    private int direction;
    private Potion currentPotion = null;
    private String potionState = "normal";
    private String mindControlState = "normal";
    private int potionTimer = 0;
    private int mindControlTimer = 0;
    private List<PlayerStateSubscriber> subscribers = new ArrayList<PlayerStateSubscriber>();
    private Position prevPosition;
    private List<BattleResponse> battles = new ArrayList<BattleResponse>();
    private int baseDamage;
    private Boolean isDead = false;
    private List<Mercenary> allies = new ArrayList<Mercenary>();
    private int enemyKillCount = 0;

    public Player(String id, String type, Position position, boolean isInteractable, int health, int direction,
            Inventory inventory, int baseDamage) {
        super(id, type, position, isInteractable);
        // super(id, type, position, isInteractable);
        this.inventory = inventory;
        this.health = health;
        this.direction = direction;
        this.prevPosition = position;
        this.baseDamage = baseDamage;
    }

    public void setPlayer(Position position, int health, int direction, String potionState, Inventory inventory) {
        setPosition(position);
        setPotionState(potionState);
        setInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void move(Direction movementDirection) {
        // just move player
        int x = this.getPosition().getX();
        int y = this.getPosition().getY();
        this.prevPosition = this.getPosition();
        if (movementDirection == Direction.UP) {
            this.setPosition(new Position(x, y - 1, 2));
        }
        if (movementDirection == Direction.DOWN) {
            this.setPosition(new Position(x, y + 1, 2));
        }
        if (movementDirection == Direction.LEFT) {
            this.setPosition(new Position(x - 1, y, 2));
        }
        if (movementDirection == Direction.RIGHT) {
            this.setPosition(new Position(x + 1, y, 2));
        }
    }
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getPotionState() {
        return potionState;
    }

    public void setPotionState(String potionState) {
        this.potionState = potionState;
    }

    public void setMindControlState(String state) {
        this.mindControlState = state;
    }
    // public List<Battle> getBattles() {
    //     return this.battles;
    // }

    // public void addBattle(Battle battle) {
    //     battles.add(battle);
    // }

    public void addBuildable(String buildable) {
        buildables.add(buildable);
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void removeBuildable(String buildable) {
        Iterator itr = buildables.iterator();
        while (itr.hasNext()) {
            String build = (String) itr.next();
            if (build.equals(buildable)) {
                itr.remove();
                break;
            }
        }
    }

    public List<String> getBuildables() {
        return this.buildables;
    }

    public void addToInventory(Item item) {
        inventory.addToInventory(item);
    }

    public void removeItemFromInventoryByType(String type) {
        inventory.removeItemFromInventoryByType(type);
    }

    public List<NonPlayerEntity> checkForOtherEntitiesOnSameCell(List<NonPlayerEntity> entities) {
        Position playerPosition = this.getPosition();
        return entities.stream().filter(ent -> ent.getPosition().equals(playerPosition)).collect(Collectors.toList());
    }

    public void build(Dungeon dungeon, String buildable, Config config) throws InvalidActionException {
        inventory.build(dungeon, buildable, config);
    }

    public List<String> getListOfBuidableItemTypes() {
        return inventory.getListOfBuidableItemTypes();
    }

    public void moveBackwards(Position oldPosition) {
        this.setPosition(oldPosition);
    }

    public void addSubscriber(PlayerStateSubscriber liveEntity) {
        subscribers.add(liveEntity);
    }

    public void notifySubscribers() { 

        decrementTimer();
        for (PlayerStateSubscriber subscriber : subscribers) {
            subscriber.notify(potionState, mindControlState, getPosition(), prevPosition, this);
        }

    }

    public void setPotionTimer(int potionDuration) {
        this.potionTimer = potionDuration;
    }

    public void setMindControlTimer(int timer) {
        this.mindControlTimer = timer;
    }
    private void decrementTimer() {
        if (mindControlTimer >= 0) {
            mindControlTimer -= 1;
            System.out.println(mindControlTimer);
        }
        
        if (mindControlTimer < 0) {
            mindControlState = "normal";
        }
        if (potionTimer >= 0) {
            potionTimer = potionTimer - 1;
        }
        
        if (potionTimer < 0) {
            // if queue is not empty 
            if (potionQueue.size() > 0) {
                currentPotion = potionQueue.remove(0);
                currentPotion.playerConsume(this);
                
            } else {
                potionState = "normal";
                currentPotion = null;
            }
        }
    }

    public void useItem(Usable item, List<NonPlayerEntity> entities, Config c) {
        item.useItem(inventory, this, entities, c);
    }

    public List<BattleItem> getBattleItems() {
        return inventory.getBattleItems();
    }

    public void initiateBattle(LiveEntity enemy, Dungeon dungeon, Position initialPosition) {
        Battle newBattle = new Battle(this, enemy);
        newBattle.simulateBattle();
        battles.add(newBattle.getBattleResponse());
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public List<BattleResponse> getBattleResponse() {
        return battles;
    }

    public void die() {
        isDead = true;
    }

    public Boolean getIsDead() {
        return isDead;
    }

    public void addAlly(Mercenary mercenary) {
        allies.add(mercenary);
    }
    public void removeAlly(Mercenary mercenary) {
        allies.remove(mercenary);
    }

    public int getNumAllies() {
        return allies.size();
    }

    public int getTotalAllyAttack() {
        if (allies.size() > 0) {
            return allies.size() * allies.get(0).getAllyattack();
        }
        return 0;
    }

    public int getTotalAllyDefence() {
        if (allies.size() > 0) {
            return allies.size() * allies.get(0).getAllyDefence();
        }
        return 0;
    }

    public Potion getCurrentPotion() {
        return currentPotion;
    }

    public void setCurrentPotion(Potion potion) {
        currentPotion = potion;
    }

    public int getPotionQueue() {
        return potionQueue.size();
    }

    public void addToPotionQueue(Potion p) {
        potionQueue.add(p);
    }

    public int getEnemyKillCount() {
        return enemyKillCount;
    }

    public void incrementEnemyKillCount() {
        this.enemyKillCount++;
    }

    public int getDirection() {
        return direction;
    }
}
