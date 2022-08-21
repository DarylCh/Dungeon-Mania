package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static dungeonmania.TestUtils.getInventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entity.NonPlayerEntity.Item.Item;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Position;

public class InventoryTests {
    @Test
    @DisplayName("Test that items are being stored in inventory")
    public void testAddingToInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_testMovementDown", "c_movementTest_testMovementDown");
        // add to inventory
        
        Item item = new Item("1", "key", new Position(1, 1), true, 1, true, false) {
        };
        dmc.currentDungeon.addToInventory(item);

        res = dmc.getDungeonResponseModel();
        assertEquals(1, getInventory(res, "key").size());
        // test removal
        dmc.currentDungeon.removeFromInventoryType("key");
        res = dmc.getDungeonResponseModel();
        assertEquals(0, getInventory(res, "key").size());
    }
}
