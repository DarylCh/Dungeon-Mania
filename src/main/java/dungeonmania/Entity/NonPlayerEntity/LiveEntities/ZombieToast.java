package dungeonmania.Entity.NonPlayerEntity.LiveEntities;
import java.util.Arrays;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.IdAssigner;
import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.ZombieToastSpawner;
import dungeonmania.util.Position;

public class ZombieToast extends LivePlayerAffectedEntity {
    public ZombieToast(String id, String type, Position position, boolean isInteractable, int health, int baseDamage, int direction){
        super(id, type, position, isInteractable, health, baseDamage, direction);
    }

    public void notify(String potionState, String mindControlState, Position playerPosition, Position prevPosition, Player player) {
        setPlayerPosition(playerPosition);
        switch (potionState) {
            case "normal":
                setState(new ZombieState(this));
                break;
            case "invisible":
                setState(new ZombieState(this));
                break;
            case "invincible":
                setState(new RunningState(this));
                break;
        }
    }

    public static void spawn (Dungeon currentDungeon) {
        List<Position> spawnerPositions = ZombieToastSpawner.getAllZombieToastSpawnerPositions(currentDungeon.getEntities());
        if (spawnerPositions.size() != 0) {
            for (Position spawnerPos : spawnerPositions) {
                List<Position> cardinalPositions = spawnerPos.getCardinallyAdjacentPositions();
                List<String> blockableEntities = Arrays.asList("wall", "boulder", "zombie_toast_spawner", "door");
                for (Position pos : cardinalPositions) {
                    if (pos.isPositionReachable(blockableEntities, currentDungeon.getEntities())) {
                        String id = IdAssigner.assignId();
                        ZombieToast zombieToast = new ZombieToast(id, "zombie_toast", pos, false, currentDungeon.getConfig().zombie_health, currentDungeon.getConfig().zombie_attack, 1);
                        currentDungeon.getPlayer().addSubscriber((PlayerStateSubscriber) zombieToast);
                        currentDungeon.addEntity(zombieToast);
                        zombieToast.setSubscriber(currentDungeon);
                        break;
                    }
                }
            }
            

        }
    }

}