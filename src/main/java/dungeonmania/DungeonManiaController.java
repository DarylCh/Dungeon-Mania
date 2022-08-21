package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.Entity.NonPlayerEntity.Item.Usable;
import dungeonmania.Entity.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.EntityHelper;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import dungeonmania.Entity.NonPlayerEntity.LiveEntities.*;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Entity.NonPlayerEntity.LiveEntities.Spider;
import dungeonmania.Goal.Goal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.Entity.*;

public class DungeonManiaController {
    public Dungeon currentDungeon;
    public int gameId = 0;
    private Random randomAssassin;
    private static Random randomHydra;
    // Change to 'test' if you want to run the blackbox testing
    public static final String filePath = "./src/main/resources/";
    public DungeonManiaController(long seed) {
        randomAssassin = new Random(seed);
        randomHydra = new Random(seed);
    }
    public DungeonManiaController() {
        this(System.currentTimeMillis());
    }
    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        return DungeonHelper.newGame(dungeonName, configName, this);
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        List<Item> inventoryItems = currentDungeon.retrieveInventory().items;
        List<ItemResponse> items = new ArrayList<ItemResponse>();
        inventoryItems.forEach(it -> {
            items.add(new ItemResponse(it.getId(), it.getType()));
        });

        List<String> listOfBuidableItemTypes = currentDungeon.getListOfBuidableItemTypes();
        Goal goals = currentDungeon.getGoals();

        return new DungeonResponse(
                this.currentDungeon.getDungeonId(),
                this.currentDungeon.getDungeonName(),
                DungeonHelper.generateEntityResponseList(currentDungeon.getEntities(),
                        currentDungeon.getPlayer()),
                items,
                currentDungeon.getPlayerBattles(),
                listOfBuidableItemTypes,
                goals.returnGoalString());
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        currentDungeon.increaseTickCount();
        Player player = currentDungeon.getPlayer();
        Item itemUsed = player.getInventory().searchInventoryById(itemUsedId);
        if (itemUsed == null) {
            player.notifySubscribers();
            moveEntities(currentDungeon.getEntities());

            // battle check
            List<NonPlayerEntity> otherEntities = player.checkForOtherEntitiesOnSameCell(currentDungeon.getEntities());
            for (NonPlayerEntity entity : otherEntities) {
                entity.playerInteract(player, currentDungeon, player.getPosition());
                if (player.getIsDead()) {
                    return getDungeonResponseModel();
                }
            }
            throw new InvalidActionException("Item is not in player's inventory");
        }
        if (itemUsed instanceof Usable) {
            player.useItem((Usable) itemUsed, currentDungeon.getEntities(), currentDungeon.getConfig());
            player.notifySubscribers();
            moveEntities(currentDungeon.getEntities());

            // battle check
            List<NonPlayerEntity> otherEntities = player.checkForOtherEntitiesOnSameCell(currentDungeon.getEntities());
            for (NonPlayerEntity entity : otherEntities) {
                entity.playerInteract(player, currentDungeon, player.getPosition());
                if (player.getIsDead()) {
                    return getDungeonResponseModel();
                }
            }
        } else {
            player.notifySubscribers();
            moveEntities(currentDungeon.getEntities());

            // battle check
            List<NonPlayerEntity> otherEntities = player.checkForOtherEntitiesOnSameCell(currentDungeon.getEntities());
            for (NonPlayerEntity entity : otherEntities) {
                entity.playerInteract(player, currentDungeon, player.getPosition());
                if (player.getIsDead()) {
                    return getDungeonResponseModel();
                }
            }
            throw new IllegalArgumentException("Item used is not a bomb or potion");
        }
        

        return getDungeonResponseModel();
    }

    private void moveEntities(List<NonPlayerEntity> entities) {
        for (Entity entity : entities) {
            System.out.println("moving" + entity);
            if (entity instanceof LiveEntity) {
                
                LiveEntity e = (LiveEntity) entity;
                e.moveNPC(currentDungeon.getEntities());
                // do something
            }
        }
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        List<NonPlayerEntity> entities = currentDungeon.getEntities();
        Player player = currentDungeon.getPlayer();

        // player movement
        Position initialPosition = player.getPosition();
        player.move(movementDirection);

        player.notifySubscribers();

        // check if anything is on the same tile as player
        List<NonPlayerEntity> otherEntities = player.checkForOtherEntitiesOnSameCell(currentDungeon.getEntities());

        // loop through each entity on the same tile
        for (NonPlayerEntity entity : otherEntities) {
            entity.playerInteract(player, currentDungeon, initialPosition);
            if (player.getIsDead()) {
                return getDungeonResponseModel();
            }
        }

        // enemy movement
        moveEntities(entities);

        otherEntities = player.checkForOtherEntitiesOnSameCell(currentDungeon.getEntities());
        for (NonPlayerEntity entity : otherEntities) {
            entity.playerInteract(player, currentDungeon, initialPosition);
            if (player.getIsDead()) {
                return getDungeonResponseModel();
            }
        }

        // spawn new enemies
        entities = currentDungeon.getEntities();
        currentDungeon.increaseTickCount(); // ignore tick 0 in spawning

        int zombie_spawn_rate = currentDungeon.getConfig().zombie_spawn_rate;

        if (zombie_spawn_rate != 0) {
            if (currentDungeon.getTickCount() % zombie_spawn_rate == 0) {
                ZombieToast.spawn(currentDungeon);
            }
        }
        int spider_spawn_rate = currentDungeon.getConfig().spider_spawn_rate;
        if (spider_spawn_rate != 0) {
            if (currentDungeon.getTickCount() % spider_spawn_rate == 0) {
                Spider.spawn(currentDungeon);
            }
        }

        // Update goals
        Goal goals = currentDungeon.getGoals();
        if (goals != null) {
            goals.setGoalAchieved(currentDungeon);
        }

        return getDungeonResponseModel();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        currentDungeon.build(buildable);
        return getDungeonResponseModel();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Player player = currentDungeon.getPlayer();
        String type = "";
        int x = 1000;
        int y = 1000;

        NonPlayerEntity npe = currentDungeon.getEntities().stream().filter(e -> e.getId().equals(entityId)).findFirst()
                .orElse(null);
        if (npe == null) {
            throw new IllegalArgumentException("Entity Id unrecognised");
        } else {
            // type = npe.getType();
            x = npe.getPosition().getX();
            y = npe.getPosition().getY();
            if (npe instanceof Mercenary) {
                Mercenary merc = (Mercenary) npe;
                // if player has spectre, bribe
                
                if(currentDungeon.retrieveInventory().checkInventoryContainsNOfType(1, "sceptre")) {
                    // swap mercenary state to allied
                    merc.setState(new AlliedState(merc));
                    merc.toggleIsEnemy();
                    merc.setIsInteractable(false);

                    // add merc to player ally list
                    currentDungeon.getPlayer().addAlly(merc);
                    // set timer
                    currentDungeon.getPlayer().setMindControlState("mind_control");
                    currentDungeon.getPlayer().setMindControlTimer(currentDungeon.getConfig().mind_control_duration);
                    // remove sceptre
                    currentDungeon.retrieveInventory()
                        .removeNItemsOfTypeFromInventory(1, "sceptre");
                } else {
                    // bribe with treasure
                    // check within radius - InvalidActionException
                int bribe_radius = currentDungeon.getConfig().bribe_radius;
                int bribe_amount = currentDungeon.getConfig().bribe_amount;
                double bribe_fail_rate = 0.0;
                if (merc instanceof Assassin) {
                    bribe_fail_rate = currentDungeon.getConfig().assassin_bribe_fail_rate;
                    bribe_amount = currentDungeon.getConfig().assassin_bribe_amount;
                }
                if (Math.abs(x - currentDungeon.getPlayer().getPosition().getX()) > bribe_radius || Math.abs(y
                        - currentDungeon.getPlayer().getPosition().getY()) > bribe_radius) {
                    throw new InvalidActionException("Merc is too far away");
                }
                // check if enough money - InvalidActionException
                if (!currentDungeon.retrieveInventory()
                        .checkInventoryContainsNOfType(bribe_amount, "treasure")) {
                    throw new InvalidActionException("Not enough treasure");
                }
                // split into merc and assassin logic
                int next = this.randomAssassin.nextInt(100);
                if (next <= 100*(1- bribe_fail_rate)) {
                    // bribe success;
                    // swap mercenary state to allied
                    merc.setState(new AlliedState(merc));
                    merc.toggleIsEnemy();
                    merc.setBribedWithTreasure(true);
                    // add merc to player ally list
                    currentDungeon.getPlayer().addAlly(merc);
                    merc.setIsInteractable(false);

                }
                // remove money
                currentDungeon.retrieveInventory()
                        .removeNItemsOfTypeFromInventory(bribe_amount, "treasure");
                }
               

                // merc does not move
            } else if (npe.getType().equals("zombie_toast_spawner")) {
                if (!EntityHelper.isCardinallyAdjacent(player, npe)) {
                    // cardianlyl adjacent - InvalidActionException
                    throw new InvalidActionException("Player is not cardinally adjcaent  to zombie  spawner");
                } else if (player.getInventory().checkInventoryContainsNOfType(1, "sword")
                        || player.getInventory().checkInventoryContainsNOfType(1, "bow")) {
                    // destroy spawner
                    player.setPosition(npe.getPosition());
                    currentDungeon.removeEntity(npe);
                } else {
                    // no weapon - InvalidActionException
                    throw new InvalidActionException("Player has no weapon to destroy spawner");
                }

            } else {
                throw new IllegalArgumentException("Entity is not a merc or spawner");
            }
        }

        return getDungeonResponseModel();
    }

    public void setDungeon(Dungeon dungeon) {
        this.currentDungeon = dungeon;
    }

    public Dungeon getDungeon() {
        return currentDungeon;
    }
    public static Random getRandomHydra() {
        return randomHydra;
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        return null;
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        return null;
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        return new ArrayList<>();
    }

}
