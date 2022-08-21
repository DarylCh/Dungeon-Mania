package dungeonmania.Entity.NonPlayerEntity.Item.BattleItem;

import dungeonmania.Battle.RoundManager;
import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.util.Position;

public abstract class BattleItem extends Item {
    private int attack;
    private int defence;

    BattleItem(String id, String type, Position position, boolean isInteractable, int durability,
            boolean isCollectable, boolean isBuildable, int attack, int defence) {
        super(id, type, position, isInteractable, durability, isCollectable, isBuildable);
        this.attack = attack;
        this.defence = defence;
    }

    public int getAttack() {
        return this.attack;
    }
    public int getDefence() {
        return this.defence;
    }

    public abstract void affectMultiplier(RoundManager manager);
}
