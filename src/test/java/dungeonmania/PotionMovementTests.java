package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entity.Player;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class PotionMovementTests {

    // // POTION MOVEMENT
    @Test
    @DisplayName("Zombie runs from player")
    public void testZombieRunFromPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_zombieTest_runFromPlayer", "c_movementTest_testMovementDown");

        // create the expected result
        Position expectedPosition0 = new Position(0, 1);
        Position expectedPosition1 = new Position(1, 1);
        Position expectedPosition2 = new Position(-1, 1);

        assertEquals(getEntities(initDungonRes, "zombie_toast").get(0).getPosition(), expectedPosition1);
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(actualDungonRes, "zombie_toast").get(0).getPosition(), expectedPosition0);
        List<ItemResponse> item_list = getInventory(actualDungonRes, "invincibility_potion");
        assertDoesNotThrow(() -> {
            DungeonResponse actualDungonRes2 = dmc.tick(item_list.get(0).getId());
            assertEquals(getEntities(actualDungonRes2, "zombie_toast").get(0).getPosition(), expectedPosition2);
        });
    }

    @Test
    @DisplayName("Merc runs from player")
    public void testMercRunFromPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_mercTest_runFromPlayer", "c_movementTest_testMovementDown");
        // create the expected result
        Position expectedPosition0 = new Position(0, 1);
        Position expectedPosition1 = new Position(1, 1);
        Position expectedPosition2 = new Position(-1, 1);

        assertEquals(getEntities(initDungonRes, "mercenary").get(0).getPosition(), expectedPosition1);
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(actualDungonRes, "mercenary").get(0).getPosition(), expectedPosition0);
        List<ItemResponse> item_list = getInventory(actualDungonRes, "invincibility_potion");
        assertDoesNotThrow(() -> {
            DungeonResponse actualDungonRes2 = dmc.tick(item_list.get(0).getId());
            assertEquals(getEntities(actualDungonRes2, "mercenary").get(0).getPosition(), expectedPosition2);
        });
    }


    @Test
    @DisplayName("Tests that potions expire after a set amount of time")
    public void testExpiry() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        assertDoesNotThrow(() -> {
            return dmc.newGame("d_basicPotionTestMerc", "c_potionTests_shortPotions");
        });
        DungeonResponse dr = dmc.tick(Direction.DOWN);
        Dungeon dungeon = dmc.getDungeon();
        Player player = dungeon.getPlayer();

        List<ItemResponse> item_list = getInventory(dr, "invisibility_potion");

        // Uses the potion
        dmc.tick(item_list.get(0).getId());
        assertEquals("invisible", player.getPotionState());
        dmc.tick(Direction.LEFT);
        assertEquals("invisible", player.getPotionState());
        dmc.tick(Direction.LEFT);
        assertEquals("normal", player.getPotionState());
    }
}
