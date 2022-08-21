package dungeonmania.Battle;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.Entity.NonPlayerEntity.Item.BattleItem.BattleItem;
import dungeonmania.Entity.NonPlayerEntity.LiveEntities.LiveEntity;
import dungeonmania.response.models.*;

public class Battle {
    private LiveEntity enemy;
    private List<Round> rounds = new ArrayList<Round>();
    private boolean isBattleEnded;
    private String winner;
    private Player player;
    private RoundManager roundManager;
    private double initialPlayerHealth;
    private double initialEnemyHealth; 

    public Battle(Player player, LiveEntity enemy) {
        this.enemy = enemy;
        this.isBattleEnded = false;
        this.winner = "none";
        this.player = player;
        this.initialEnemyHealth = enemy.getHp();
        this.initialPlayerHealth = player.getHealth();
        this.roundManager = new RoundManager(this, player.getHealth(), enemy.getHp(), player.getPotionState(), player.getTotalAllyAttack(), player.getTotalAllyDefence(), player.getCurrentPotion(), enemy);
    }

    public LiveEntity getEnemy() {
        return enemy;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void simulateBattle() {
        roundManager.simulateBattle();
    }

    public List<RoundResponse> getRoundsResponses() {
        List<RoundResponse> responses = new ArrayList<>();

        for (Round round : this.rounds) {
            responses.add(new RoundResponse(round.getDeltaCharacterHealth(), round.getDeltaEnemyHealth(),
                    round.getWeaponryUsedResponses()));
        }
        return responses;
    }

    public boolean isBattleEnded() {
        return this.isBattleEnded;
    }

    public void setIsBattleEnded(boolean isBattleEnded) {
        this.isBattleEnded = isBattleEnded;
    }

    public String getWinner() {
        return this.winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<BattleItem> getBattleItems() {
        return player.getBattleItems();
    }

    public double getPlayerDamage() {
        return player.getBaseDamage();
    }

    public int getEnemyDamage() {
        return enemy.getBaseDamage();
    }

    public void destroyItem(Item item) {
        player.removeItemFromInventoryByType(item.getType());
    }

    public void killPlayer() {
        setWinner(enemy.getType());
        player.die();
    }

    public void killEnemy() {
        player.incrementEnemyKillCount();
        setWinner("player");
        enemy.informRemoval();
    }

    public void addRound(Round round) {
        rounds.add(round);
    }

    public BattleResponse getBattleResponse() {
        return new BattleResponse(enemy.getType(), getRoundsResponses(), initialPlayerHealth, initialEnemyHealth);
    }

    public void setPlayerHealth(double health) {
        player.setHealth(health);
    }
}


