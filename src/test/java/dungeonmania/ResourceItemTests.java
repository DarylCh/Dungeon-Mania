package dungeonmania;

import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class ResourceItemTests {
    
    @Test
    @DisplayName("Test player can succesfully pick up each resource type (treasure, a key, wood and an arrow)")
    public void succesfullyPickUpResources() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();

        // I didn't make a custom config file because it seems too trivial
        DungeonResponse res = dmc.newGame("d_resourceTest_pickUpResources", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "treasure").size());
        // Indirectly test that the treasure was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "treasure").size()); 

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        // Indirectly test that the key was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());

        // pick up wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        // Indirectly test that the wood was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());

        // pick up arrow
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "arrow").size());
        // Indirectly test that the arrow was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "arrow").size());
    }

    @Test
    @DisplayName("Test player can only carry one key at a time")
    public void playerCanOnlyCarryOneKeyAtATime() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_resourceTest_pickUpTwoKeys", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        
        // pick up the first key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        // Indirectly test that the key was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());

        // attempt to pick up a second key
        res = dmc.tick(Direction.RIGHT);
        // Indirectly test that the second key was NOT removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());

        // I don't think an exception is required here

        // Assert that only 1 key exists in the inventory
        assertEquals(1, getInventory(res, "key").size());
    }

    // @Test
    // @DisplayName("Test key disappears after being used to open a door")
    // public void keyDisappearsAfterUsedToOpenDoor() {     
    // }
    // Note: useKeyWalkThroughOpenDoor() from ExampleTests.java already tests this

    @Test
    @DisplayName("Test wood, key and treasure disappear after being used to craft shields")
    public void woodKeyTreasureDisappearAfterCraftingShield() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_resourceTest_craftingShield", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up 4 x wood
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(4, getInventory(res, "wood").size());
        // Indirectly test that all 4 x wood has been removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(4, getInventory(res, "wood").size());

        // pick up 1 x treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "treasure").size());
        // Indirectly test that the treasure has been removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "treasure").size());

        // craft shield
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        // check that 2 x wood and 1 x treasure was removed from inventory
        assertEquals(2, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "treasure").size());
        // check that shield was added to the inventory
        assertEquals(1, getInventory(res, "shield").size());

        // pick up 1 x key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        // Indirectly test that the key has been removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());

        // craft shield
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        // check that 2 x wood and 1 x key was removed from inventory
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "treasure").size());
        // check that (a second) shield was added to the inventory
        assertEquals(2, getInventory(res, "shield").size());

    }

    @Test
    @DisplayName("Test wood and arrows disappear after being used to craft a bow")
    public void woodArrowsDisappearAfterCraftingBow() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_resourceTest_craftingBow", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up 1 x wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        // Indirectly test that the wood was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());

        // pick up 3 x arrows
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, getInventory(res, "arrow").size());
        // Indirectly test that the 3 x arrow was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, getInventory(res, "arrow").size());

        // craft bow and check that 1 x wood and 3 x arrows was removed from inventory
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        // check that 2 x wood and 1 x arrow was removed from inventory
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "arrow").size());
        // check that bow was added to the inventory
        assertEquals(1, getInventory(res, "bow").size());
    }

    @Test
    @DisplayName("Test trying to build an invalid buildable throws an exception")
    public void invalidBuildable() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_resourceTest_craftingShield", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // attempt to build invalid buildable
        assertThrows(IllegalArgumentException.class, () -> {
            dmc.build("invalidBuildable");
        });
    }

    @Test
    @DisplayName("Test building a bow without sufficient resources")
    public void testBuildBowWithoutSufficientResources() {

        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_resourceTest_craftingBow", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up 1 x wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        // Indirectly test that the wood was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());

        // pick up 1 x arrows (3 arrows would be required to build)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "arrow").size());
        // Indirectly test that the 1 x arrow was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "arrow").size());

        // make a failed attempt to craft a bow
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("bow");
        });
    }

    @Test
    @DisplayName("Test building a shield without sufficient resources")
    public void testBuildShieldWithoutSufficientResources() {

        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_resourceTest_craftShieldWithoutSufficientResources", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up 1 x wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        // Indirectly test that all 1 x wood has been removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());

        // pick up 1 x treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "treasure").size());
        // Indirectly test that the treasure has been removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "treasure").size());

        // pick up 1 x key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        // Indirectly test that the treasure has been removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());

        // make a failed attempt to craft a shield
        assertThrows(InvalidActionException.class, () -> {
            dmc.build("shield");
        });
    }
}
