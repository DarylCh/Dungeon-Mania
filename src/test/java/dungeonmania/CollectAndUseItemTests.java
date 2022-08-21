package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CollectAndUseItemTests {
    @Test
    @DisplayName("Test player can succesfully pick up each resource type (treasure, a key, wood and an arrow)")
    public void collectResources() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_resourceTest_pickUpResources",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up treasure
        assertEquals(1, getEntities(res, "treasure").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "treasure").size());
        assertEquals(0, getEntities(res, "treasure").size());
        // Indirectly test that the treasure was removed from the map after being picked
        // up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "treasure").size());

        // pick up key
        assertEquals(1, getEntities(res, "key").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(0, getEntities(res, "key").size());
        // Indirectly test that the key was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());

        // pick up wood
        assertEquals(1, getEntities(res, "wood").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(0, getEntities(res, "wood").size());
        // Indirectly test that the wood was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());

        // pick up arrow
        assertEquals(1, getEntities(res, "arrow").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "arrow").size());
        assertEquals(0, getEntities(res, "arrow").size());
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
        assertEquals(2, getEntities(res, "key").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getEntities(res, "key").size());

        // attempt to pick up a second key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getEntities(res, "key").size());

        // Assert that only 1 key exists in the inventory
        assertEquals(1, getInventory(res, "key").size());
    }

    @Test
    @DisplayName("Test walking past non collectable")
    public void ignoringNonCollectable() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_collectAndUseItemTest_nonCollectable",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // walk past floor switch
        assertEquals(1, getEntities(res, "switch").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, getInventory(res, "switch").size());
        assertEquals(1, getEntities(res, "switch").size());
    }

    @Test
    @DisplayName("Test use item exceptions")
    public void useItemExceptions() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_resourceTest_pickUpResources",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        assertThrows(InvalidActionException.class, () -> {
            dmc.tick("invalid_id");
        });
        // pick up the first key
        assertEquals(1, getEntities(res, "treasure").size());
        res = dmc.tick(Direction.RIGHT);
        String treasureId = getInventory(res, "treasure").get(0).getId();

        assertThrows(IllegalArgumentException.class, () -> {
            dmc.tick(treasureId);
        });
    }

    @Test
    @DisplayName("Test that the map still ticks after an attempt (invalid) to use an item")
    public void useItemFailTick() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        
        DungeonResponse res = dmc.newGame("d_mercTest_bribe",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        res = dmc.tick(Direction.RIGHT);
        String treasureId = getEntities(res, "treasure").get(0).getId();
        assertThrows(InvalidActionException.class, () -> {
            DungeonResponse res2 = dmc.tick(treasureId);
            // check that merc still moved
            assertEquals(getEntities(res2, "mercenary").get(0).getPosition(), new Position(3, 0));   
        });
        
    }
    @Test
    @DisplayName("Test that the map still ticks after an attempt (invalid) to use an item")
    public void useItemFailTick2() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercTest_bribe",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        assertThrows(InvalidActionException.class, () -> {
            DungeonResponse res2 = dmc.tick("invalid_id");
            // check that merc still moved
            assertEquals(getEntities(res2, "mercenary").get(0).getPosition(), new Position(4, 0));   
        });
        
    }

}
