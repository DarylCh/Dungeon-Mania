package dungeonmania.Entity.NonPlayerEntity.Item;

import java.util.List;

import dungeonmania.Config;
import dungeonmania.Inventory;
import dungeonmania.Entity.Player;
import dungeonmania.Entity.NonPlayerEntity.*;

public interface Usable {
    public void useItem(Inventory inventory, Player player, List<NonPlayerEntity> entities, Config c);
}
