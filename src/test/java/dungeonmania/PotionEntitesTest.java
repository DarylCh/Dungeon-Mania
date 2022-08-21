package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entity.Player;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;


public class PotionEntitesTest {
    @Test
    @DisplayName("Tests that the invincibility potion can be picked up")
    public void testPickupInvincibility() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertDoesNotThrow(() -> {
            return dmc.newGame("d_basicPotionTest", "c_potionTests_longPotions");
        });
        DungeonResponse dr = dmc.tick(Direction.UP);
        List<ItemResponse> item_list = getInventory(dr, "invincibility_potion");
        
        // This invincibility potion should be the only item in the list
        assertEquals(item_list.get(0).getType(), "invincibility_potion");
        
        // Check that the item has been picked up
        assertEquals(new ArrayList<EntityResponse>(), getEntities(dr, "invincibility_potion"));
        
        dr = dmc.tick(Direction.DOWN);
    }

    @Test
    @DisplayName("Tests that the invisibility potion can be picked up")
    public void testPickupInvisibility() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertDoesNotThrow(() -> {
            return dmc.newGame("d_basicPotionTest", "c_potionTests_longPotions");
        });
        DungeonResponse dr = dmc.tick(Direction.DOWN);
        List<ItemResponse> item_list = getInventory(dr, "invisibility_potion");
        // This invisiblity potion should be the only item in the list
        assertEquals(item_list.get(0).getType(), "invisibility_potion");

        // Check that the item has been picked up
        assertEquals(new ArrayList<EntityResponse>(), getEntities(dr, "invisibility_potion"));
    }

    @Test
    @DisplayName("Tests that potions expire after a set amount of time")
    public void testExpiry() throws InvalidActionException {
        // TODO
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

    // @Test
    // @DisplayName("Tests that the potion stack tick mechanism works")
    // public void testPotionStackTick() throws InvalidActionException {
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     assertDoesNotThrow(() -> {
    //         return dmc.newGame("d_PotionTest_stacking", "c_potionTests_shortPotions");
    //     });
    //     Player player = dmc.getDungeon().getPlayer();

    //     DungeonResponse res = dmc.tick(Direction.RIGHT);
    //     res = dmc.tick(Direction.RIGHT);
    //     List<ItemResponse> item_list_1 = getInventory(res, "invincibility_potion");
    //     assertEquals("normal", player.getPotionState());
    //     res = dmc.tick(Direction.RIGHT);
    //     List<ItemResponse> item_list_2 = getInventory(res, "invisibility_potion");
    //     assertEquals("normal", player.getPotionState());
    //     res = assertDoesNotThrow(() -> dmc.tick(item_list_1.get(0).getId()));
    //     assertEquals("invincible", player.getPotionState());
    //     res = assertDoesNotThrow(() -> dmc.tick(item_list_2.get(0).getId()));
    //     assertEquals("invincible", player.getPotionState());

    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals("invisible", player.getPotionState());
    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals("invisible", player.getPotionState());
    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals("normal", player.getPotionState());


    //}

    

    @Test
    @DisplayName("Tests that the player cannot consume potions unless they exist in the inventory")
    public void testConsumeNothing() {
        
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicPotionTest",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        assertEquals(1, getEntities(res, "invisibility_potion").size());

        assertThrows(InvalidActionException.class, () -> {
            dmc.tick(getEntities(dmc.newGame("d_basicPotionTest",
            "c_DoorsKeysTest_useKeyWalkThroughOpenDoor"), "invisibility_potion").get(0).getId());
        });

    }
}
