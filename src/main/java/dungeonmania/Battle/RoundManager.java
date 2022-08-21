package dungeonmania.Battle;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.lang.Math;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.Entity.NonPlayerEntity.Item.BattleItem.BattleItem;
import dungeonmania.Entity.NonPlayerEntity.Item.Potion.Potion;
import dungeonmania.Entity.NonPlayerEntity.LiveEntities.Hydra;
import dungeonmania.Entity.NonPlayerEntity.LiveEntities.LiveEntity;

public class RoundManager {
    private Battle battle;
    private int swordModifier = 0;
    private int bowModifier = 0;
    private int shieldModifier = 0;
    private int armourAttackModifier = 0;
    private int armourDefenceModifier = 0;
    private double totalAllyAttack;
    private double totalAllyDefence;
    private double playerHealth;
    private double enemyHealth;
    private double totalPlayerAttack;
    private double totalEnemyAttack;
    private List<BattleItem> battleItems;
    private Boolean over = false;
    private String playerState;
    private Potion currentPotion;
    private LiveEntity enemy;
    
    public RoundManager(Battle battle, double playerHealth, int enemyHealth, String playerState, int totalAllyAttack, int totalAllyDefence, Potion currentPotion, LiveEntity enemy) {
        this.battle = battle;
        this.playerHealth = playerHealth;
        this.enemyHealth = enemyHealth;
        this.battleItems = battle.getBattleItems();
        this.playerState = playerState;
        this.totalAllyAttack = totalAllyAttack;
        this.totalAllyDefence = totalAllyDefence;
        this.currentPotion = currentPotion;
        this.enemy = enemy;
    }

    public void simulateBattle() {
        ListIterator<BattleItem> iter = battleItems.listIterator();
        while (iter.hasNext()) {
            BattleItem item = iter.next();
            item.affectMultiplier(this);
            
            if (item.getDurability() <= 0) {
                battle.destroyItem(item);
            }   
        }
        calculateAttackModifiers();
        while (!over) {
            playerHealth -= totalEnemyAttack;
            enemyHealth -= totalPlayerAttack;
            System.out.println(enemyHealth);
            battle.addRound(new Round(-totalEnemyAttack, -totalPlayerAttack, generateItemsUsed()));
            if (playerHealth <= 0 || enemyHealth <= 0) {
                over = true;
            }
            if (playerHealth <= 0) {
                battle.killPlayer();
            } 
            else if (enemyHealth <= 0) {
                battle.killEnemy();
                battle.setPlayerHealth(playerHealth);
            }
        }
        // Round newRound = new Round(deltaPlayerHealth, deltaEnemyHealth, weaponryUsed);
    }

    private void calculateAttackModifiers() {
        //System.out.println(enemy.getType());
        if (playerState.equals("invincible")) {
            totalPlayerAttack = enemyHealth;
            totalEnemyAttack = 0;
        } else if (enemy.getType().equals("hydra")) {
            double rate  = ((Hydra) enemy).getHealthIncreaseRate();
            System.out.println("hydra");
            if (DungeonManiaController.getRandomHydra().nextInt(100) <= 100*rate) {
                totalPlayerAttack = -((Hydra) enemy).getHealthIncreaseAmount();
            } else {
                double rawDamage = ((double) battle.getPlayerDamage() + (double) swordModifier + (double) totalAllyAttack + (double) armourAttackModifier) * Math.pow(2, (double) bowModifier); 
                totalPlayerAttack = rawDamage / 5;
            }
            double enemyDmg = ((double) battle.getEnemyDamage() - (double) shieldModifier - (double) totalAllyDefence - (double) armourDefenceModifier) / 10;
            totalEnemyAttack = enemyDmg <= 0 ? 0 : enemyDmg;
        }
        else {
            double rawDamage = ((double) battle.getPlayerDamage() + (double) swordModifier + (double) totalAllyAttack + (double) armourAttackModifier) * Math.pow(2, (double) bowModifier); 
            totalPlayerAttack = rawDamage / 5;
            double enemyDmg = ((double) battle.getEnemyDamage() - (double) shieldModifier - (double) totalAllyDefence - (double) armourDefenceModifier) / 10;
            totalEnemyAttack = enemyDmg <= 0 ? 0 : enemyDmg;
        }

    }

    public void incrementBowModifier() {
        bowModifier += 1;
    }

    public void incrementSwordModifier(int damage) {
        swordModifier += damage;
    }

    public void incrementShieldModifier(int defence) {
        shieldModifier += defence;
    }
    public void incrementArmourModifier(int attack, int defence) {
        armourAttackModifier += attack;
        armourDefenceModifier += defence;
    }
    
    
    private List<Item> generateItemsUsed() {
        List<Item> itemsUsed = new ArrayList<Item>();
        for (BattleItem battleItem : battleItems) {
            itemsUsed.add(battleItem);
        }
        if (playerState.equals("invincible")) {
            itemsUsed.add(currentPotion);
        }
        return itemsUsed;
    }

}
