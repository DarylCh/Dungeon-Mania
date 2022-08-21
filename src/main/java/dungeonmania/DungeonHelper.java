package dungeonmania;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import dungeonmania.Entity.Entity;
import dungeonmania.Entity.EntityFactory;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Entity.NonPlayerEntity.LiveEntities.LiveEntity;
import dungeonmania.Goal.Goal;
import dungeonmania.Goal.GoalComplex.GoalComposeAND;
import dungeonmania.Goal.GoalComplex.GoalComposeOR;
import dungeonmania.Goal.GoalSimple.GoalBoulders;
import dungeonmania.Goal.GoalSimple.GoalEnemies;
import dungeonmania.Goal.GoalSimple.GoalExit;
import dungeonmania.Goal.GoalSimple.GoalSimple;
import dungeonmania.Goal.GoalSimple.GoalTreasure;
import dungeonmania.Entity.Player;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;
import spark.utils.IOUtils;

public class DungeonHelper {
    private static final String mainFilePath = "./src/main/resources/";
    private static final String testFilePath = "./src/test/resources/";
    private static int gameId = 0;

    public static DungeonResponse newGame(String dungeonName, String configName, DungeonManiaController controller)
            throws IllegalArgumentException {
        String dungeonPath = mainFilePath + "dungeons/" + dungeonName + ".json";
        String configPath = mainFilePath + "configs/" + configName + ".json";

        File dungeon = new File(dungeonPath);
        File config = new File(configPath);

        if (!dungeon.exists() || !config.exists()) {
            dungeonPath = testFilePath + "dungeons/" + dungeonName + ".json";
            configPath = testFilePath + "configs/" + configName + ".json";
            dungeon = new File(dungeonPath);
            config = new File(configPath);
            if (!dungeon.exists() || !config.exists()) {
                throw new IllegalArgumentException("Dungeon/Config file not found");
            }
        }

        try {
            return initializeGame(dungeonName, dungeonPath, configPath, controller);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error occured while reading json files");
        }
    }

    private static DungeonResponse initializeGame(String dungeonName, String dungeonPath, String configPath,
            DungeonManiaController controller) throws IOException {
        InputStream ISDungeon = new FileInputStream(dungeonPath);
        String jsonTxtDungeon = IOUtils.toString(ISDungeon);
        JSONObject json = new JSONObject(jsonTxtDungeon);

        InputStream ISConfig = new FileInputStream(configPath);
        String jsonTxtConfig = IOUtils.toString(ISConfig);
        JSONObject jsonConfig = new JSONObject(jsonTxtConfig);

        List<NonPlayerEntity> allEntities = new ArrayList<NonPlayerEntity>();
        List<EntityResponse> allEntitiesResponse = new ArrayList<EntityResponse>();
        Config c = new Config(jsonConfig);
        Player player = generateEntities(dungeonPath, json, allEntities, allEntitiesResponse, c);
        
        JSONObject goalCondition = json.getJSONObject("goal-condition");       
        Goal goals = generateGoals(goalCondition);

        DungeonResponse newGame = new DungeonResponse(String.valueOf(gameId), dungeonName,
                allEntitiesResponse, new ArrayList<ItemResponse>(), new ArrayList<BattleResponse>(),
                new ArrayList<String>(), goals.returnGoalString());
        
        Dungeon newDungeon = new Dungeon(String.valueOf(gameId), dungeonName, allEntities, c, player, goals);

        for (NonPlayerEntity entity : allEntities) {
            if (entity instanceof LiveEntity) {
                LiveEntity e = (LiveEntity) entity;
                e.setSubscriber(newDungeon);
            }
        }
    
        controller.setDungeon(newDungeon);
        return newGame;
    }

    private static Player generateEntities(String dungeon_path, JSONObject json, List<NonPlayerEntity> allEntities,
            List<EntityResponse> allEntitesResponse, Config c) throws IOException {
        JSONArray jArray = json.getJSONArray("entities");
        Player player = null;
        for (int i = 0; i < jArray.length(); i++) {
            String type = jArray.getJSONObject(i).getString("type");
            if (type.equals("player")) {
                Position p = new Position(jArray.getJSONObject(i).getInt("x"), jArray.getJSONObject(i).getInt("y"));
                player = new Player("0", type, p, false, c.player_health, 1, new Inventory(), c.player_attack);
                allEntitesResponse.add(new EntityResponse(player.getId(), type, p, false));
                break;
            }
        }
        if (player == null) {
            throw new IOException("Player could not be initialised");
        }

        for (int i = 0; i < jArray.length(); i++) {
            JSONObject eDict = jArray.getJSONObject(i);
            String type = eDict.getString("type");
            Position p = new Position(eDict.getInt("x"), eDict.getInt("y"));
            String entity_id = "0";

            if (type.equals("player")) {
                continue;
            }
            else if (type.equals("door") || type.equals("key")) {
                entity_id = IdAssigner.assignId();
                int key_id = eDict.getInt("key");

                NonPlayerEntity entity = EntityFactory.createEntity(entity_id, type, p,
                        false, c, key_id);
                allEntities.add(entity);
            } else if (type.equals("portal")) {
                entity_id = IdAssigner.assignId();
            
                String colour = eDict.getString("colour");

                NonPlayerEntity entity = EntityFactory.createEntity(entity_id, type, p,
                        false, c, colour);
                allEntities.add(entity);
            } else {
                entity_id = IdAssigner.assignId();
                NonPlayerEntity entity = EntityFactory.createEntity(entity_id, type, p, type == "zombie_toast" ? true : false, c, player, allEntities);
                allEntities.add(entity);
            }

            EntityResponse entityResp = new EntityResponse(entity_id, type, p,
                    type.equals("zombie_toast") ? true : false);

            allEntitesResponse.add(entityResp);
        }
        return player;
    }

    public static List<EntityResponse> generateEntityResponseList(List<NonPlayerEntity> entities, Player player) {
        List<EntityResponse> allEntitiesResponse = new ArrayList<EntityResponse>();
        for (Entity entity : entities) {
            EntityResponse entityResp = new EntityResponse(entity.getId(), entity.getType(), entity.getPosition(),
                    entity.getIsInteractable());
            allEntitiesResponse.add(entityResp);
        }
        if (!(player.getIsDead())) {
            allEntitiesResponse.add(
                new EntityResponse(player.getId(), player.getType(), player.getPosition(), player.getIsInteractable()));
        
        }
        return allEntitiesResponse;
    }

    public static Goal generateGoals(JSONObject json) {        
        String goal = json.getString("goal");

        switch(goal) {
            case "OR":
                return generateORGoal(json);
            case "AND":
                return generateANDGoal(json);
            default:
                return generateSimpleGoal(goal);
        }        
    }

    public static GoalSimple generateSimpleGoal(String simpleGoal) {
        switch (simpleGoal) {
            case "enemies":
                return new GoalEnemies();
            case "boulders":
                return new GoalBoulders();
            case "treasure":
                return new GoalTreasure();
            case "exit":
                return new GoalExit();
            default:
                return null;
        }
    }

    public static GoalComposeOR generateORGoal(JSONObject json) {
        JSONArray subgoalsArray = json.getJSONArray("subgoals");

        GoalComposeOR GoalOR = new GoalComposeOR();

        for (int i = 0; i < subgoalsArray.length(); i++) {
            JSONObject subgoalObject = subgoalsArray.getJSONObject(i);
            Goal subgoal = generateGoals(subgoalObject);
            GoalOR.addToListOfGoals(subgoal);
        }

        return GoalOR;
    }

    public static GoalComposeAND generateANDGoal(JSONObject json) {
        JSONArray subgoalsArray = json.getJSONArray("subgoals");

        GoalComposeAND GoalAND = new GoalComposeAND();

        for (int i = 0; i < subgoalsArray.length(); i++) {
            JSONObject subgoalObject = subgoalsArray.getJSONObject(i);
            Goal subgoal = generateGoals(subgoalObject);
            GoalAND.addToListOfGoals(subgoal);
        }

        return GoalAND;
    }


}
