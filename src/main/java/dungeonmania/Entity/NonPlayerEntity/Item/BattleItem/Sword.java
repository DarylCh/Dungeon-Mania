package dungeonmania.Entity.NonPlayerEntity.Item.BattleItem;

import dungeonmania.Battle.RoundManager;
import dungeonmania.util.Position;

public class Sword extends BattleItem {
    public Sword(String id, String type, Position position, boolean isInteractable, int durability,
            boolean isCollectable, boolean isBuildable, int attack, int defence) {
        super(id, type, position, isInteractable, durability, isCollectable, isBuildable, attack, defence);
    }

    public void affectMultiplier(RoundManager manager) {
        manager.incrementSwordModifier(this.getAttack());
        setDurability(getDurability() - 1);
        
    }
}
