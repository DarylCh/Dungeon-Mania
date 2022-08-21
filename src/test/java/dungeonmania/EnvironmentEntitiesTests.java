package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entity.NonPlayerEntity.EnvironmentalEntity.FloorSwitch;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;

public class EnvironmentEntitiesTests {

    @Test
    @DisplayName("Test player cannot walk through a wall")
    public void wallBlock() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_wallMovement", "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");
        Position pos0 = getEntities(res, "player").get(0).getPosition();
        // walk into wall
        res = dmc.tick(Direction.RIGHT);
        Position pos1 = getEntities(res, "player").get(0).getPosition();

        assertEquals(pos0, pos1);

    }

    @Test
    @DisplayName("Player has wrong key")
    public void playerFailsWalkThroughDoorWithKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeysTest_Fail",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(1, getInventory(res, "key").size());

        // walk through door and check key is not gone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "key").size());
        // check that player can't walk through the door
        assertEquals(pos, getEntities(res, "player").get(0).getPosition());

    }

    @Test
    @DisplayName("Test player can use a key to open and walk through a door")
    public void useKeyToOpenDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeysTest_useKeyWalkThroughOpenDoor",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        Position pos = getEntities(res, "player").get(0).getPosition();
        assertEquals(1, getInventory(res, "key").size());

        // walk through door and check key is gone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, getInventory(res, "key").size());
        assertNotEquals(pos, getEntities(res, "player").get(0).getPosition());

    }

    @Test
    @DisplayName("Player has no key")
    public void playerFailsWalkThroughDoorWithNoKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeysTst_noKey",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        res = dmc.tick(Direction.RIGHT);
        Position pos = getEntities(res, "player").get(0).getPosition();

        // check player cant walk through door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(pos, getEntities(res, "player").get(0).getPosition());

    }

    @Test
    @DisplayName("Test player can push boulder")
    public void playerPushBoulder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");

        Position pos = getEntities(res, "boulder").get(0).getPosition();
        // Activate Switch
        res = dmc.tick(Direction.RIGHT);
        Position pos2 = getEntities(res, "player").get(0).getPosition();
        Position pos3 = getEntities(res, "boulder").get(0).getPosition();

        // assertEquals(pos, pos2);
        // assertNotEquals(pos3, pos2);
        assertTrue(pos.equals(pos2));
        assertFalse(pos3.equals(pos2));
    }

    @Test
    @DisplayName("Test player can't push two boulders")
    public void playerCantPushTwoBoulders() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderTest_cantPushTwoBoulders", "c_bombTest_placeBombRadius2");

        // Initial positions from dungeon json
        // player (2, 2) | boulder0 (3, 2) | <emptytile> (4, 2)
        // <emptytile> (2, 3) | boulder1 (3, 3) | <emptytile> (4, 3)
        // <emptytile> (2, 4) | boulder2 (3, 4) | <emptytile> (4, 4)
        // <emptytile> (2, 5) | <emptytile> (3, 5) | <emptytile> (4, 5)

        // push boulder0 to position (4, 2)
        res = dmc.tick(Direction.RIGHT);
        Position posPlayer = getEntities(res, "player").get(0).getPosition();
        Position posBoulder0 = getEntities(res, "boulder").get(0).getPosition();
        // assertEquals(posPlayer, new Position(3, 2));
        // assertEquals(posBoulder0, new Position(4, 2));
        assertTrue(posPlayer.equals(new Position(3, 2)));
        assertTrue(posBoulder0.equals(new Position(4, 2)));

        // make a failed attempt to push boulder 1 to position (3, 4)
        res = dmc.tick(Direction.DOWN);
        posPlayer = getEntities(res, "player").get(0).getPosition();
        Position posBoulder1 = getEntities(res, "boulder").get(1).getPosition();
        Position posBoulder2 = getEntities(res, "boulder").get(2).getPosition();
        // assertEquals(posPlayer, new Position(3, 2));
        // assertEquals(posBoulder1, new Position(3, 3));
        // assertEquals(posBoulder2, new Position(3, 4));
        assertTrue(posPlayer.equals(new Position(3, 2)));
        assertTrue(posBoulder1.equals(new Position(3, 3)));
        assertTrue(posBoulder2.equals(new Position(3, 4)));
    }

    @Test
    @DisplayName("Test player can't push boulder onto wall")
    public void playerCantPushBoulderOntoWall() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderTest_cantPushBoulderOntoWall", "c_bombTest_placeBombRadius2");

        // Initial positions
        // player (2, 2) | boulder0 (3, 2) | <emptytile> (4, 2) | wall (5, 2)

        // push boulder0 to position (4, 2)
        res = dmc.tick(Direction.RIGHT);
        Position posPlayer = getEntities(res, "player").get(0).getPosition();
        Position posBoulder = getEntities(res, "boulder").get(0).getPosition();
        // assertEquals(posPlayer, new Position(3, 2));
        // assertEquals(posBoulder, new Position(4, 2));
        assertTrue(posPlayer.equals(new Position(3, 2)));
        assertTrue(posBoulder.equals(new Position(4, 2)));

        // make a failed attempt to push boulder onto wall (position (5, 2))
        res = dmc.tick(Direction.RIGHT);
        posPlayer = getEntities(res, "player").get(0).getPosition();
        posBoulder = getEntities(res, "boulder").get(0).getPosition();
        Position posWall = getEntities(res, "wall").get(0).getPosition();
        // assertEquals(posPlayer, new Position(3, 2));
        // assertEquals(posBoulder, new Position(4, 2));
        // assertEquals(posWall, new Position(5, 2));
        assertTrue(posPlayer.equals(new Position(3, 2)));
        assertTrue(posBoulder.equals(new Position(4, 2)));
        assertTrue(posWall.equals(new Position(5, 2)));
    }

    @Test
    @DisplayName("Test player can push boulder onto collectable entities")
    public void playerCanPushBouldersOntoCollectables() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_boulderTest_canPushOntoCollectables", "c_bombTest_placeBombRadius2");

        // Initial positions
        // player starts at (0, 1)
        // directly below is: boulder, treasure, key, invincibility_potion,
        // invisibility_potion, wood, arrow, bomb, sword
        // each on their own tile

        // push boulder onto each collectable
        List<String> types = Arrays.asList("treasure", "key", "invincibility_potion",
                "invisibility_potion", "wood", "arrow", "bomb", "sword");
        for (int i = 1; i < 9; ++i) {
            res = dmc.tick(Direction.DOWN);
            Position posPlayer = getEntities(res, "player").get(0).getPosition();
            Position posBoulder = getEntities(res, "boulder").get(0).getPosition();
            Position posCollectable = getEntities(res, types.get(i - 1)).get(0).getPosition();
            assertEquals(posPlayer, new Position(0, i));
            assertEquals(posBoulder, new Position(0, i + 1));
            assertEquals(posCollectable, new Position(0, i + 1));

            assertTrue(posPlayer.equals(new Position(0, i)));
            assertTrue(posBoulder.equals(new Position(0, i + 1)));
            assertTrue(posCollectable.equals(new Position(0, i + 1)));
        }
    }

    @Test
    @DisplayName("Test floor switch activated")
    public void testSwitchActivation() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_bombTest_placeBombRadius2",
                "c_bombTest_placeBombRadius2");

        // Activate Switch
        dmc.tick(Direction.RIGHT);
        FloorSwitch floorSwitch = dmc.currentDungeon.getEntities().stream().filter(e -> e.getType().equals("switch"))
                .map(FloorSwitch.class::cast).findFirst().orElse(null);
        assertEquals(true, floorSwitch.getIsActive());
    }

    @Test
    @DisplayName("Test player can teleport")
    public void playerTeleports() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalTest_basic", "c_bombTest_placeBombRadius2");

        // Player starts at (0, 1)
        // Corresponding portals at (1, 1) and (3, 1)

        Position intialPosition = getEntities(res, "player").get(0).getPosition();
        assertTrue(intialPosition.equals(new Position(0, 1)));

        // Player moves RIGHT onto portal at (1, 1)
        res = dmc.tick(Direction.RIGHT);

        // Final position is one of the cardinally adjacent positions of the
        // corresponding portal
        Position finalPosition = getEntities(res, "player").get(0).getPosition();
        Position final1 = new Position(3, 0);
        Position final2 = new Position(4, 1);
        Position final3 = new Position(3, 2);
        Position final4 = new Position(2, 1);
        assertTrue(Set.of(final1, final2, final3, final4).contains(finalPosition));
    }

    @Test
    @DisplayName("Test player uses bomb that isn't in inventory")
    public void playerUsesBombNotInInventory() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalTest_BombIntegration",
                "c_bombTest_placeBombRadius1");

        assertEquals(0, getInventory(res, "bomb").size());
        assertThrows(InvalidActionException.class, () -> {
            dmc.tick(getEntities(dmc.newGame("d_portalTest_BombIntegration",
                    "c_bombTest_placeBombRadius1"), "bomb").get(0).getId());
        });
    }

    @Test
    @DisplayName("Test surrounding entities are removed when placing a bomb next then activate switch with config file bomb radius set to 2")
    public void placeBombRadius2_activation_after_placement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2",
                "c_bombTest_placeBombRadius2");

        // Activate Switch

        // Pick up Bomb
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "bomb").size());

        // Place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        // Check Bomb exploded with radius 2
        //
        // Boulder/Switch Wall Wall
        // Bomb Treasure
        //
        // Treasure
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(0, getEntities(res, "wall").size());
        assertEquals(0, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }

    @Test
    @DisplayName("Test surrounding entities are not removed when placing a bomb next to an unactivated switch")
    public void placeBombRadius2_deactivated() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTest_placeBombRadius2_wall_removed",
                "c_bombTest_placeBombRadius2");

        // Activate Switch

        // Pick up Bomb

        res = dmc.tick(Direction.RIGHT);
        FloorSwitch floorSwitch = dmc.currentDungeon.getEntities().stream().filter(e -> e.getType().equals("switch"))
                .map(FloorSwitch.class::cast).findFirst().orElse(null);
        assertEquals(true, floorSwitch.getIsActive());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(false, floorSwitch.getIsActive());

        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);

        // Place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));
        // check bomb didn't work
        assertEquals(1, getEntities(res, "bomb").size());
        assertEquals(1, getEntities(res, "boulder").size());
        assertEquals(1, getEntities(res, "switch").size());
        assertEquals(1, getEntities(res, "wall").size());
        assertEquals(2, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());
    }

    @Test
    @DisplayName("Test player can't teleport due to bomb explosion")
    public void playerTeleportBomb() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalTest_BombIntegration",
                "c_bombTest_placeBombRadius1");

        // Activate Switch
        res = dmc.tick(Direction.RIGHT);

        // Pick up Bomb
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(res, "bomb").size());

        // Place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        String bombId = getInventory(res, "bomb").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.tick(bombId));

        // Check Bomb exploded with radius 2
        //
        // Boulder/Switch Wall Wall
        // Bomb Treasure
        //
        // Treasure
        assertEquals(1, getEntities(res, "portal").size());
        assertEquals(0, getEntities(res, "bomb").size());
        assertEquals(0, getEntities(res, "boulder").size());
        assertEquals(0, getEntities(res, "switch").size());
        assertEquals(1, getEntities(res, "wall").size());
        assertEquals(1, getEntities(res, "treasure").size());
        assertEquals(1, getEntities(res, "player").size());

        // go back to portal to the left of original player position
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(getEntities(res, "portal").get(0).getPosition(), getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @DisplayName("Test player avoids walls when teleporting")
    public void playerTeleportConstrainedByWalls() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalTest_avoidWalls", "c_bombTest_placeBombRadius2");

        // Initial dungeon setup
        // EXIT (0, 0) | WALL (1, 0) | WALL (2, 0) | empty (3, 0) | empty (4, 0)
        // PLAYER (0, 1) | PORTAL (1, 1) | WALL (2, 1) | PORTAL (3, 1) | WALL (4, 1)
        // WALL (0, 2) | WALL (1, 2) | WALL (2, 2) | empty (3, 2) | empty (4, 2)

        getEntities(res, "player").get(0).getPosition();

        // Player moves RIGHT onto portal at (1, 1)
        res = dmc.tick(Direction.RIGHT);
        Position finalPosition = getEntities(res, "player").get(0).getPosition();

        // The player has not teleported on any of the walls cardinally adjacent to the
        // corresponding portal
        Position wall1 = new Position(2, 1);
        Position wall2 = new Position(4, 1);
        assertFalse(Set.of(wall1, wall2).contains(finalPosition));

        // Final position is one of the non-wall cardinally adjacent positions of the
        // corresponding portal
        Position final1 = new Position(3, 0);
        Position final2 = new Position(3, 2);
        assertTrue(Set.of(final1, final2).contains(finalPosition));
    }

    @Test
    @DisplayName("Test player cannot teleport at all because all cardinally-adjacent squares are walls")
    public void playerCantTeleportOntoWall() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalTest_cantTeleportOntoWall", "c_bombTest_placeBombRadius2");

        // Initial dungeon setup
        // EXIT (0, 0) | WALL (1, 0) | WALL (2, 0) | WALL (3, 0) | empty (4, 0)
        // PLAYER (0, 1) | PORTAL (1, 1) | WALL (2, 1) | PORTAL (3, 1) | WALL (4, 1)
        // WALL (0, 2) | WALL (1, 2) | WALL (2, 2) | WALL (3, 2) | empty (4, 2)

        getEntities(res, "player").get(0).getPosition();

        // Player moves RIGHT onto portal at (1, 1)
        res = dmc.tick(Direction.RIGHT);
        Position portalPosition = getEntities(res, "player").get(0).getPosition();
        Position finalPosition = getEntities(res, "player").get(0).getPosition();

        // The player has not teleported on any of the walls cardinally adjacent to the
        // corresponding portal
        Position final1 = new Position(3, 0);
        Position final2 = new Position(4, 1);
        Position final3 = new Position(3, 2);
        Position final4 = new Position(2, 1);
        assertFalse(Set.of(final1, final2, final3, final4).contains(finalPosition));

        // The player stayed where they were
        assertTrue(portalPosition.equals(finalPosition));
    }

    @Test
    @DisplayName("Test player teleports through a chain of 2 portal-pairs")
    public void playerTestTeleportChainedPortalsSimple() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalTest_chainedPortalsSimple", "c_bombTest_placeBombRadius2");

        // Initial dungeon setup

        // EXIT(0, 0) | empty(1, 0) | empty(2, 0) | WALL(3, 0) | empty (4, 0) | empty(5,
        // 0) | final1 (6, 0) | empty (7, 0)
        // PLAYER(0, 1) | RED(1, 1) | WALL(2, 1) | RED(3, 1) | BLUE (4, 1) | final4(5,
        // 1) | BLUE (6, 1) | final2 (7, 1)
        // empty(0, 2) | empty(1, 2) | empty(2, 2) | WALL(3, 2) | empty (4, 2) | empty
        // (5, 2) | final3 (6, 2) | empty (7, 2)

        // Player moves RIGHT onto RED portal at (1, 1)
        res = dmc.tick(Direction.RIGHT);
        // Player teleports through RED (3,1) to BLUE (4, 1) through BLUE(6,1) to one of
        // the final four cardinally adjacent positions

        // Final position is one of the cardinally adjacent positions of the
        // corresponding portal
        Position finalPosition = getEntities(res, "player").get(0).getPosition();
        Position final1 = new Position(6, 0);
        Position final2 = new Position(7, 1);
        Position final3 = new Position(6, 2);
        Position final4 = new Position(5, 1);
        assertTrue(Set.of(final1, final2, final3, final4).contains(finalPosition));
    }

    // @Test
    // @DisplayName("Test player failing to teleport stays there")
    // public void playerTestTeleportChainedPortalsBackwards() {
    // DungeonManiaController dmc;
    // dmc = new DungeonManiaController();
    // DungeonResponse res =
    // dmc.newGame("d_portalTest_chainedPortalsBackwards","c_bombTest_placeBombRadius2");

    // // Initial dungeon setup

    // // EXIT(0, 0) | final1(1, 0) | empty(2, 0) | WALL(3, 0) | empty (4, 0) |
    // empty(5, 0) | WALL (6, 0) | empty (7, 0)
    // // PLAYER(0, 1) | RED(1, 1) | WALL(2, 1) | RED(3, 1) | BLUE (4, 1) | WALL(5,
    // 1) | BLUE (6, 1) | WALL (7, 1)
    // // empty(0, 2) | final2(1, 2) | empty(2, 2) | WALL(3, 2) | empty (4, 2) |
    // empty (5, 2) | WALL (6, 2) | empty (7, 2)

    // // Player moves RIGHT onto RED portal at (1, 1)
    // res = dmc.tick(Direction.RIGHT);
    // // Player teleports through RED (3,1) to BLUE (4, 1), fails to teleport via
    // blue, so steps back onto RED(3,1) and teleports back to a cardinally adjacent
    // // square to RED(1,1)

    // // Final position is one of the cardinally adjacent positions of the
    // corresponding portal
    // Position finalPosition = getEntities(res, "player").get(0).getPosition();
    // Position final1 = new Position(1, 0);
    // Position final2 = new Position(1, 2);
    // Position final3 = new Position(0, 1);
    // assertTrue(Set.of(final1, final2, final3).contains(finalPosition));
    // assertTrue(portalPosition.equals(finalPosition));
    // }

    @Test
    @DisplayName("Test player teleports through a chain of 3 portal-pairs all surrounded by walls")
    public void playerTestTeleportChainedPortals() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_portalTest_chainedPortals", "c_bombTest_placeBombRadius2");

        // Initial dungeon setup
        // EXIT (0, 0) | WALL (1, 0) | WALL (2, 0) | WALL (3, 0) | empty (4, 0) | empty
        // (5, 0)
        // PLAYER (0, 1) | RED (1, 1) | empty (2, 1) | ORANGE (3, 1) | WALL (4, 1) |
        // empty (5, 1)
        // WALL (0, 2) | WALL (1, 2) | WALL (2, 2) | WALL (3, 2) | empty (4, 2) | empty
        // (5, 2)
        // WALL (0, 3) | RED (1, 3) | WALL (2, 3) | WALL (3, 3) | WALL (4, 3) | WALL (5,
        // 3)
        // WALL (0, 4) | BLUE (1, 4) | WALL (2, 4) | BLUE (3, 4) | ORANGE (4, 4) | WALL
        // (5, 4)
        // empty (0, 5) | WALL (1, 5) | empty (2, 5) | WALL (3, 5) | WALL (4, 5) | WALL
        // (5, 5)

        // Player moves RIGHT onto RED portal at (1, 1)
        res = dmc.tick(Direction.RIGHT);
        // Player teleports through RED (1,3) to BLUE (1, 4)
        // then through BLUE (3, 4) to ORANGE (4, 4)
        // then through ORANGE (3, 1) to the empty square at (2, 1)

        Position finalPosition = getEntities(res, "player").get(0).getPosition();
        assert (finalPosition.equals(new Position(2, 1)));
    }

    @Test
    @DisplayName("Test player has weapon and destroy zombie toast spawner")
    public void playerHasSwordDestroyZombieToastSpawner() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombie_toast_spawn_destroy", "c_bombTest_placeBombRadius2");
        // Initial dungeon setup
        // PLAYER (0, 1) | Sword (1, 1) | ZombieToastSpawner (2, 1) | Bow (3, 1) |
        // Zombie Toast Spawner (4, 1) |
        String swordid = getEntities(res, "sword").get(0).getId();
        assertThrows(IllegalArgumentException.class, () -> dmc.interact(swordid));
        String id = getEntities(res, "zombie_toast_spawner").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(id));
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());
        res = dmc.tick(Direction.RIGHT);
        res = dmc.interact(id);

        assertEquals(0, getEntities(res, "zombie_toast_spawner").size());
    }

    @Test
    @DisplayName("Test wood and arrows disappear after being used to craft a bow then destroy zombie toast spawner")
    public void woodArrowsDisappearAfterCraftingBow() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawn_destroy_bow",
                "c_DoorsKeysTest_useKeyWalkThroughOpenDoor");

        // pick up 1 x wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        // Indirectly test that the wood was removed from the map after being picked up
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res, "wood").size());
        assertEquals(1, getEntities(res, "zombie_toast_spawner").size());

        // pick up 3 x arrows
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, getInventory(res, "arrow").size());
        // Indirectly test that the 3 x arrow was removed from the map after being
        // picked up
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
        String id = getEntities(res, "zombie_toast_spawner").get(0).getId();
        Position initialposition = getEntities(res, "zombie_toast_spawner").get(0).getPosition();
        res = dmc.interact(id);
        assertEquals(0, getEntities(res, "zombie_toast_spawner").size());
        assertEquals(initialposition, getEntities(res, "player").get(0).getPosition());

    }

    @Test
    @DisplayName("Test player has no weapon so cannot destroy zombie toast spawner")
    public void playerFailWeaponDestroyZombieToastSpawner() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombie_toast_spawn_destroy_fail", "c_bombTest_placeBombRadius2");

        // Initial dungeon setup
        // PLAYER (0, 1) | Empty | ZombieToastSpawner (2, 1) | Empty | Zombie Toast
        // Spawner (4, 1) |
        assertEquals(2, getEntities(res, "zombie_toast_spawner").size());
        res = dmc.tick(Direction.RIGHT);
        Position initialPosition = getEntities(res, "player").get(0).getPosition();
        String id = getEntities(res, "zombie_toast_spawner").get(0).getId();
        assertThrows(InvalidActionException.class, () -> dmc.interact(id));
        Position finalPosition = getEntities(res, "player").get(0).getPosition();
        assertEquals(2, getEntities(res, "zombie_toast_spawner").size());
        assertEquals(initialPosition, finalPosition);
    }
}

// @Test
// @DisplayName("Test spider can walk on wall")
// public void spiderWallWalk() {
// DungeonManiaController dmc;
// dmc = new DungeonManiaController();
// DungeonResponse res = dmc.newGame("d_spiderTest_basicMovement",
// "c_spiderTest_basicMovement");

// // need to run spider spawn function at specific position, let it go up by
// one
// // spot onto wall after character moves
// res = dmc.tick(Direction.UP);
// Position pos = getEntities(res, "spider").get(0).getPosition();
// Position pos2 = getEntities(res, "Wall").get(0).getPosition();
// assertEquals(pos, pos2);
// }

// @Test
// @DisplayName("Test player can teleport")
// public void playerTeleports() {
// DungeonManiaController dmc;
// dmc = new DungeonManiaController();
// DungeonResponse res = dmc.newGame(" d_portalTest_basic",
// "c_bombTest_placeBombRadius2");

// Position pos1 = getEntities(res, "player").get(0).getPosition();
// res = dmc.tick(Direction.DOWN);
// Position pos2 = getEntities(res, "player").get(0).getPosition();
// assertEquals(pos1.getX() + 2, pos2.getX());
// assertEquals(pos1.getY(), pos2.getY());

// }

// @Test
// @DisplayName("Test player cannot teleport into wall")
// public void playerTeleportFails() {
// DungeonManiaController dmc;
// dmc = new DungeonManiaController();
// DungeonResponse res = dmc.newGame("d_portalWallTest_Fail",
// "c_bombTest_placeBombRadius2");

// Position pos1 = getEntities(res, "player").get(0).getPosition();
// res = dmc.tick(Direction.DOWN);
// Position pos2 = getEntities(res, "player").get(0).getPosition();

// assertNotEquals(pos1.getX() + 2, pos2.getX());
// assertNotEquals(pos1.getY(), pos2.getY());
// assertEquals(pos1, pos2);
// }

// need tests for zombie spawner*/
