package dungeonmania.Entity;

import java.util.List;
import dungeonmania.*;
import dungeonmania.Entity.NonPlayerEntity.NonPlayerEntity;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.Boulder;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.Door;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.Exit;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.FloorSwitch;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.Portal;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.Wall;
import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.ZombieToastSpawner;
import dungeonmania.Entity.NonPlayerEntity.Item.Bomb;
import dungeonmania.Entity.NonPlayerEntity.Item.BattleItem.Sword;
import dungeonmania.Entity.NonPlayerEntity.Item.ResourceItem.Key;
import dungeonmania.Entity.NonPlayerEntity.Item.ResourceItem.*;
import dungeonmania.Entity.NonPlayerEntity.Item.Potion.*;
import dungeonmania.Entity.NonPlayerEntity.LiveEntities.*;
import dungeonmania.util.Position;

public class EntityFactory {
    public static NonPlayerEntity createEntity(String entity_id, String type, Position p, Boolean isInteractable,
            Config c, Player player, List<NonPlayerEntity> entities) throws NullPointerException {
        NonPlayerEntity entity = null;
        int direction = 1;
        switch (type) {
            case "spider":
                entity = new Spider(entity_id, type, p, isInteractable, c.spider_health, c.spider_attack, direction);
                break;
            case "zombie_toast":
                entity = new ZombieToast(entity_id, type, p, isInteractable, c.zombie_health, c.zombie_attack, direction);
                player.addSubscriber((PlayerStateSubscriber) entity);
                break;
            case "mercenary":
                entity = new Mercenary(entity_id, type, p, true, c.mercenary_health, c.mercenary_attack, c.ally_attack, c.ally_defence, direction);
                player.addSubscriber((PlayerStateSubscriber) entity);
                break;
            case "assassin":
                entity = new Assassin(entity_id, type, p, true, c.assassin_health, c.assassin_attack, c.ally_attack, c.ally_defence, direction,
                c.assassin_bribe_amount,c.assassin_bribe_fail_rate, c.assassin_recon_radius);
                player.addSubscriber((PlayerStateSubscriber) entity);
                break;
            case "hydra":
                entity = new Hydra(entity_id, type, p, isInteractable, c.hydra_health, c.hydra_attack, direction, c.hydra_health_increase_rate, c.hydra_health_increase_amount);
                player.addSubscriber((PlayerStateSubscriber) entity);
                break;
            case "wall":
                entity = new Wall(entity_id, type, p, isInteractable);
                break;
            case "exit":
                entity = new Exit(entity_id, type, p, isInteractable);
                break;
            case "boulder":
                entity = new Boulder(entity_id, type, p, isInteractable, false);
                break;
            case "switch":
                entity = new FloorSwitch(entity_id, type, p, isInteractable, false);
                break;
            case "zombie_toast_spawner":
                entity = new ZombieToastSpawner(entity_id, type, p, isInteractable);
                break;
            case "arrow":
                entity = new Arrow(entity_id, type, p, isInteractable, direction, true, false);
                break;
            case "treasure":
                entity = new Treasure(entity_id, type, p, isInteractable, direction, true, false);
                break;
            case "invincibility_potion":
                entity = new InvincibilityPotion(entity_id, type, p, isInteractable, direction, true, false, c.invincibility_potion_duration);
                break;
            case "invisibility_potion":
                entity = new InvisibilityPotion(entity_id, type, p, isInteractable, direction, true, false, c.invisibility_potion_duration);
                break;
            case "wood":
                entity = new Wood(entity_id, type, p, isInteractable, direction, true, false);
                break;
            case "bomb":
                entity = new Bomb(entity_id, type, p, isInteractable, 1, true, false, c.bomb_radius);
                break;
            case "sword":
                entity = new Sword(entity_id, type, p, isInteractable, c.sword_durability, true, false, c.sword_attack, 0);
                break;

            case "sun_stone":
                entity = new SunStone(entity_id, type, p, isInteractable, direction, true, false);
            break;
        }
        if (entity == null) {
            throw new NullPointerException("no class for " + type);
        }
        return entity;
    }

    public static NonPlayerEntity createEntity(String entity_id, String type, Position p, Boolean isInteractable,
            Config c, int key_id) throws NullPointerException {
        NonPlayerEntity entity = null;
        int direction = 1;
        switch (type) {

            case "door":
                entity = new Door(entity_id, type, p, isInteractable, false, key_id);
                break;

            case "key":
                entity = new Key(entity_id, type, p, isInteractable, direction, true, false, key_id);
                break;

        }
        if (entity == null)

        {
            throw new NullPointerException("no class for " + type);
        }
        return entity;

    }

    public static NonPlayerEntity createEntity(String entity_id, String type, Position p, Boolean isInteractable,
            Config c, String colour) {
        NonPlayerEntity entity = null;
        entity = new Portal(entity_id, type, p, isInteractable, colour);
        return entity;
    }

}
