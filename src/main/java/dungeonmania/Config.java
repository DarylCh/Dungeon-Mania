package dungeonmania;

import org.json.JSONObject;

public class Config {
    private static final int INVALID = -42;
    public final int ally_attack;
    public final int ally_defence;
    public final int bomb_radius;
    public final int bow_durability;
    public final int bribe_amount;
    public final int bribe_radius;
    public final int enemy_goal;
    public final int invincibility_potion_duration;
    public final int invisibility_potion_duration;
    public final int mercenary_attack;
    public final int mercenary_health;
    public final int player_attack;
    public final int player_health;
    public final int shield_defence;
    public final int shield_durability;
    public final int spider_attack;
    public final int spider_health;
    public final int spider_spawn_rate;
    public final int sword_attack;
    public final int sword_durability;
    public final int treasure_goal;
    public final int zombie_attack;
    public final int zombie_health;
    public final int zombie_spawn_rate;
    
    public int assassin_attack = INVALID;
    public int assassin_bribe_amount = INVALID;
    public double assassin_bribe_fail_rate = INVALID;
    public int assassin_health = INVALID;
    public int assassin_recon_radius = INVALID;
    public int hydra_attack = INVALID;
    public int hydra_health = INVALID;
    public double hydra_health_increase_rate = INVALID;
    public int hydra_health_increase_amount = INVALID;
    public int mind_control_duration = INVALID;
    public int midnight_armour_attack = INVALID;
    public int midnight_armour_defence = INVALID;


    Config(JSONObject json) {
        this.ally_attack = json.getInt("ally_attack");
        this.ally_defence = json.getInt("ally_defence");
        this.bomb_radius = json.getInt("bomb_radius");
        this.bow_durability = json.getInt("bow_durability");
        this.bribe_amount = json.getInt("bribe_amount");
        this.bribe_radius = json.getInt("bribe_radius");
        this.enemy_goal = json.getInt("enemy_goal");
        this.invincibility_potion_duration = json.getInt("invincibility_potion_duration");
        this.invisibility_potion_duration = json.getInt("invisibility_potion_duration");
        this.mercenary_attack = json.getInt("mercenary_attack");
        this.mercenary_health = json.getInt("mercenary_health");
        this.player_attack = json.getInt("player_attack");
        this.player_health = json.getInt("player_health");
        this.shield_defence = json.getInt("shield_defence");
        this.shield_durability = json.getInt("shield_durability");
        this.spider_attack = json.getInt("spider_attack");
        this.spider_health = json.getInt("spider_health");
        this.spider_spawn_rate = json.getInt("spider_spawn_rate");
        this.sword_attack = json.getInt("sword_attack");
        this.sword_durability = json.getInt("sword_durability");
        this.treasure_goal = json.getInt("treasure_goal");
        this.zombie_attack = json.getInt("zombie_attack");
        this.zombie_health = json.getInt("zombie_health");
        this.zombie_spawn_rate = json.getInt("zombie_spawn_rate");
        if (json.has("assassin_attack")) {
            this.assassin_attack = json.getInt("assassin_attack");
        }
        if (json.has("assassin_bribe_amount")) {
            this.assassin_bribe_amount = json.getInt("assassin_bribe_amount");
        }
        if (json.has("assassin_bribe_fail_rate")) {
            this.assassin_bribe_fail_rate = json.getDouble("assassin_bribe_fail_rate");
        }
        if (json.has("assassin_health")) {
            this.assassin_health = json.getInt("assassin_health");
        }

        if (json.has("assassin_recon_radius")) {
            this.assassin_recon_radius = json.getInt("assassin_recon_radius");
        }
        if (json.has("hydra_attack")) {
            this.hydra_attack = json.getInt("hydra_attack");
        }
        if (json.has("hydra_health")) {
            this.hydra_health = json.getInt("hydra_health");
        }
        if (json.has("hydra_health_increase_rate")) {
            this.hydra_health_increase_rate = json.getDouble("hydra_health_increase_rate");
        }
        if (json.has("hydra_health_increase_amount")) {
            this.hydra_health_increase_amount = json.getInt("hydra_health_increase_amount");
        }
        if (json.has("mind_control_duration")) {
            this.mind_control_duration = json.getInt("mind_control_duration");
        }
        if (json.has("midnight_armour_attack")) {
            this.midnight_armour_attack = json.getInt("midnight_armour_attack");
        }
        if (json.has("midnight_armour_defence")) {
            this.midnight_armour_defence = json.getInt("midnight_armour_defence");
        }

    
        
        
    
        
       
        
        
        
    }
}
