package dungeonmania.Battle;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.response.models.ItemResponse;

public class Round {
    private double deltaPlayerHealth;
    private double deltaEnemyHealth;
    private List<Item> weaponryUsed;

    public Round(double deltaPlayerHealth, double deltaEnemyHealth, List<Item> weaponryUsed) {
        this.deltaPlayerHealth = deltaPlayerHealth;
        this.deltaEnemyHealth = deltaEnemyHealth;
        this.weaponryUsed = weaponryUsed;
    }

    public double getDeltaCharacterHealth() {
        return deltaPlayerHealth;
    }

    public double getDeltaEnemyHealth() {
        return deltaEnemyHealth;
    }

    public List<Item> getWeaponryUsed() {
        return weaponryUsed;
    }

    public List<ItemResponse> getWeaponryUsedResponses() {
        List<ItemResponse> responses = new ArrayList<>();
        for (Item item : weaponryUsed) {
            responses.add(new ItemResponse(item.getId(), item.getType()));
        }
        return responses;
    }
}
