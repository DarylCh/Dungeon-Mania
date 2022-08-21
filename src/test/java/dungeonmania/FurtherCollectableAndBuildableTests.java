package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FurtherCollectableAndBuildableTests {
    // SUNSTONE
    @Test
    @DisplayName("Test player can succesfully pick up sunstone")
    public void collectSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneTest_pickUp",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up treasure
        assertEquals(1, getEntities(res, "sun_stone").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(0, getEntities(res, "sun_stone").size());
        
    }

    @Test
    @DisplayName("Test player can open door with sunstone and retains it")
    public void openDoorWithSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneTest_pickUp",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(1, getInventory(res, "sun_stone").size());

        // walk through door and check sunstone is still there
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
        // check that door opened
        res = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(res, "player").get(0).getPosition(), new Position(4, 1));
    }

    @Test
    @DisplayName("Test that if a player has the right key and a sunstone, the sunstone is used to open the door")
    public void openDoorWithSunstoneOrKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneTest_openDoorWithKey",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());

        // walk through door and check sunstone is used instead of the key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        assertEquals(1, getInventory(res, "sun_stone").size());

        // check we can actually walk through door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(res, "player").get(0).getPosition(), new Position(5, 1));
        
    }
    @Test
    @DisplayName("Test simple treasure goal with sunstone counting as one treasure")
    public void goalSunstoneTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneTest_treasureGoal","c_simpleGoal_fourTreasure");
        
        assertTrue(getGoals(res).contains(":treasure"));

        // Initial dungeon setup
        // (0, 0)	        (1, 0)	        (2, 0)      	(3, 0)	        (4, 0)	        (5, 0)
        // (0, 1)	        (1, 1)	        (2, 1)TREASURE	(3, 1)	        (4, 1)	        (5, 1)TREASURE
        // (0, 2)      	    (1, 2)SUNSTONE	(2, 2)PLAYER	(3, 2)TREASURE	(4, 2)      	(5, 2)TREASURE    <-- unused treasure
        // (0, 3)	        (1, 3)	        (2, 3)TREASURE	(3, 3)	        (4, 3)	        (5, 3)TREASURE
        // (0, 4)	        (1, 4)	        (2, 4)      	(3, 4)	        (4, 4)	        (5, 4)

        // collect treasure
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        assertEquals(":treasure", getGoals(res));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(":treasure", getGoals(res));

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        assertEquals(":treasure", getGoals(res));

        res = dmc.tick(Direction.LEFT);

        // goal successfully achieved
        assertEquals("", getGoals(res));
    }

    // TODO Check that you can't bribe mercs or assassins
    // BUILDABLES
    @Test
    @DisplayName("Test wood disappears after being used to craft shield, but sunstone is retained")
    public void craftingShieldWithSunstone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneTest_craftingShield", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up 2 x wood
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
    
        assertEquals(2, getInventory(res, "wood").size());
        // pick up 1 x sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
    
        // craft shield
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        // check that 2 x wood was removed from inventory, but sunstone is retained
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        // check that shield was added to the inventory
        assertEquals(1, getInventory(res, "shield").size());
    }

    // MIDNIGHT ARMOUR
    @Test
    @DisplayName("Test sword and sun stone can be used to craft midnight armour")
    public void craftingMidnightArmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmour_craft", "c_midnightArmourTest_zombieSpawn");

        // pick up 1 x sword
        res = dmc.tick(Direction.RIGHT);
    
        assertEquals(1, getInventory(res, "sword").size());
        // pick up 1 x sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
    
        // craft midnight armour
        
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        // check that 2 x wood was removed from inventory, but sunstone is retained
        assertEquals(0, getInventory(res, "sword").size());
        assertEquals(0, getInventory(res, "sun_stone").size());
        // check that shield was added to the inventory
        assertEquals(1, getInventory(res, "midnight_armour").size());
    }

    
    @Test
    @DisplayName("Test midnight armour craft fail because zombies present")
    public void craftingMidnightArmourFailBecauseZombies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmour_craft", "c_midnightArmourTest_zombieSpawn");

        // pick up 1 x sword
        res = dmc.tick(Direction.RIGHT);
    
        assertEquals(1, getInventory(res, "sword").size());
        // pick up 1 x sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
    
        // craft midnight armour
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getEntities(res, "zombie_toast").size());
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
        
    }
    @Test
    @DisplayName("Test midnight armour craft fail because insufficient resources")
    public void craftingMidnightArmourFailBecauseInsufficientResources() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmour_craft", "c_midnightArmourTest_zombieSpawn");

        // pick up 1 x sword
        res = dmc.tick(Direction.RIGHT);
    
        assertEquals(1, getInventory(res, "sword").size());
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }

    // SCEPTRE
    @Test
    @DisplayName("Test crafting sceptre")
    public void craftingSceptre() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptre_craft", "c_midnightArmourTest_zombieSpawn");

        
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
    
        // craft sceptre
        
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        // check that all removed
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "treasure").size());
        assertEquals(0, getInventory(res, "sun_stone").size());
        // check added to inventory
        assertEquals(1, getInventory(res, "sceptre").size());


        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "treasure").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
    
        // craft sceptre
        
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        // check that all removed
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(0, getInventory(res, "key").size());
        assertEquals(0, getInventory(res, "sun_stone").size());
        assertEquals(2, getInventory(res, "sceptre").size());

        


        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "arrow").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "arrow").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
    
        // craft sceptre
        
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        // check that all removed
        assertEquals(0, getInventory(res, "arrow").size());
        assertEquals(0, getInventory(res, "key").size());
        assertEquals(0, getInventory(res, "sun_stone").size());
        assertEquals(3, getInventory(res, "sceptre").size());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "arrow").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "arrow").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "treasure").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
    
        // craft sceptre
        
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        // check that all removed
        assertEquals(0, getInventory(res, "arrow").size());
        assertEquals(0, getInventory(res, "treasure").size());
        assertEquals(0, getInventory(res, "sun_stone").size());
        assertEquals(4, getInventory(res, "sceptre").size());
    }


    // craft sceptre using (1 wood OR 2 arrows) + (1 sunstone) + (1 sun stone) 
    // (1 sunstone is consumed and one is retained)
    @Test
    @DisplayName("Test crafting sceptre with sunstone as substitute for treasure or key")
    public void craftingSceptreWithSunstone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptre_craft_sunstone", "c_midnightArmourTest_zombieSpawn");

        
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "sun_stone").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "sun_stone").size());
    
        // craft sceptre
        
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        // check that all removed
        assertEquals(0, getInventory(res, "wood").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        // check added to inventory
        assertEquals(1, getInventory(res, "sceptre").size());


        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "arrow").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "arrow").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, getInventory(res, "sun_stone").size());
    
        // craft sceptre
        
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        // check that all removed
        assertEquals(0, getInventory(res, "arrow").size());
        assertEquals(1, getInventory(res, "sun_stone").size());
        assertEquals(2, getInventory(res, "sceptre").size());
    }
    

    // Use sceptre to bribe - check this is consumed after use
    @Test
    @DisplayName("merc bribe with sceptre")
    public void testMercBribeWithSceptre() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptre_bribe", "c_mercTests_bribe3");
        
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        DungeonResponse res2 = assertDoesNotThrow(() -> dmc.build("sceptre"));
        // check added to inventory
        assertEquals(1, getInventory(res2, "sceptre").size());
        assertEquals(getEntities(res, "player").get(0).getPosition(), new Position(3, 0));
        assertEquals(getEntities(res, "mercenary").get(0).getPosition(), new Position(4, 0));
        assertDoesNotThrow(() -> {
            DungeonResponse resBribe = dmc.interact(getEntities(res2, "mercenary").get(0).getId());
            assertEquals(0, getInventory(resBribe, "sceptre").size());
            assertEquals(getEntities(resBribe, "mercenary").get(0).getPosition(), new Position(4, 0));
            assertEquals(getEntities(resBribe, "player").get(0).getPosition(), new Position(3, 0));
        });
        // merc follows for 3 counts
        res = dmc.tick(Direction.LEFT); // tick 1
        assertEquals(getEntities(res, "player").get(0).getPosition(), new Position(2, 0));
        assertEquals(getEntities(res, "mercenary").get(0).getPosition(), new Position(3, 0));
        res = dmc.tick(Direction.RIGHT); // tick 2
        assertEquals(getEntities(res, "player").get(0).getPosition(), new Position(3, 0));
        assertEquals(getEntities(res, "mercenary").get(0).getPosition(), new Position(2, 0));
        res = dmc.tick(Direction.LEFT); // tick 3
        assertEquals(getEntities(res, "player").get(0).getPosition(), new Position(2, 0));
        assertEquals(getEntities(res, "mercenary").get(0).getPosition(), new Position(3, 0));
        res = dmc.tick(Direction.RIGHT); // no longer mind controlled
        assertEquals(0, getEntities(res, "mercenary").size());




        
    }
    // check bribery works from far away
    // 
}
