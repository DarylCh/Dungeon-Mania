package dungeonmania;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.util.Position;
import dungeonmania.Entity.Player;
import dungeonmania.Goal.Goal;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.Portal;
import dungeonmania.Entity.*;

public class Dungeon implements EntitySubscriber {
    private String dungeonId;
    private String dungeonName;
    // private String goals;
    private Goal goals;
    private List<NonPlayerEntity> entities;
    private Config config;
    private Player player;
    private int tickCount = 0;

    public Dungeon(String dungeonId, String dungeonName, List<NonPlayerEntity> allEntities, Config config,
            Player player, Goal goals) {
        this.dungeonId = dungeonId;
        this.dungeonName = dungeonName;
        this.entities = allEntities;
        this.config = config;
        this.player = player;
        this.goals = goals;
    }

    public Player getPlayer() {
        return player;
    }

    public String getDungeonName() {
        return this.dungeonName;
    }

    public String getDungeonId() {
        return this.dungeonId;
    }

    public final Goal getGoals() {
        return goals;
    }

    public final String getGoalString() {
        return goals.returnGoalString();
    }

    public final List<NonPlayerEntity> getEntities() {
        return entities;
    }

    public Config getConfig() {
        return config;
    }

    public Inventory retrieveInventory() {
        return player.getInventory();
    }

    public void addToInventory(Item item) {
        player.addToInventory(item);
    }

    public void removeFromInventoryType(String type) {
        player.removeItemFromInventoryByType(type);
    }

    public void addEntity(NonPlayerEntity entity) {
        this.entities.add(entity);
    }
    public void removeEntity(NonPlayerEntity entity) {
        this.entities.remove(entity);
    }

    public void build(String buildable) throws InvalidActionException {
        player.build(this, buildable, config);
    }

    public List<String> getListOfBuidableItemTypes() {
        return player.getListOfBuidableItemTypes();
    }

    public void informRemoval(NonPlayerEntity entity) {
        entities.remove(entity);
    }

    public List<BattleResponse> getPlayerBattles() {
        return player.getBattleResponse();
    }
    public void increaseTickCount() {
        this.tickCount++;
    }

    public int getTickCount() {
        return this.tickCount;
    }
    public Portal isThereAPortalHere(Position position) {
        List<Portal> portals = this.getEntities().stream()
                .filter(it -> it.getType().equals("portal")).map(Portal.class::cast)
                .collect(Collectors.toList());
        List<Portal> portalsAtPosition = portals.stream()
                .filter(it -> it.getPosition().equals(position)).collect(Collectors.toList());

        if (portalsAtPosition.size() == 1) {

            return portalsAtPosition.get(0);
        }
        return null;
    }

}
