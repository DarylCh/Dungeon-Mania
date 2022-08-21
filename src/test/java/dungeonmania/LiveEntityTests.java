package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LiveEntityTests {
    
    //@DisplayName("Create Player")
    /*public void testCreatePlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_playerTest_createPlayer", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // create the expected result
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);

        EntityResponse actualPlayer = getPlayer(initDungonRes).get();

        assertEquals(expectedPlayer, actualPlayer);
    }*/
    @Test
    @DisplayName("Spider move")
    public void testMoveSpider() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res= dmc.newGame("d_spiderTest_BasicSpiderMove", "c_movementTest_testMovementDown");

        // create the expected result
        
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 0));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(2, 0));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(2, 1));
    }
    @Test
    @DisplayName("Spider move into boulder and bounce")
    public void testMoveSpiderBoulder() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_boulderBounce", "c_movementTest_testMovementDown");

        // create the expected result
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 0));
        res= dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(0, 0));
    }
    @Test
    @DisplayName("Spider stuck between boulders")
    public void testMoveSpiderBoulderStuck() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_boulderStuck", "c_movementTest_testMovementDown");

        // create the expected result
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 0));
    }
    @Test
    @DisplayName("Spider spawn below boulder")
    public void testMoveSpiderBoulderAbove() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_boulderAbove", "c_movementTest_testMovementDown");

        // create the expected result
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(0, 1));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(0, 2));
    }
    @Test
    @DisplayName("Zombie move simple")
    public void testMoveZombie() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieTest_simple", "c_movementTest_testMovementDown");


        // create the expected result
    
        assertEquals(getEntities(res, "zombie").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
        Position finalPosition = getEntities(res, "zombie").get(0).getPosition();

        // zombie is in one of the cardinally adjacent positions
        Position final1 = new Position(1, 0);
        Position final2 = new Position(1, 2);
        Position final3 = new Position(0, 1);
        Position final4 = new Position(2, 1);
        assertTrue(Set.of(final1, final2, final3, final4).contains(finalPosition));
        

    }
    @Test
    @DisplayName("Zombie move restricted by walls")
    public void testZombieMoveOnePossible() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res= dmc.newGame("d_zombieTest_onePossible", "c_movementTest_testMovementDown");
    
        assertEquals(getEntities(res, "zombie").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
       
        // position expected 1,0
        assertEquals(getEntities(res, "zombie").get(0).getPosition(), new Position(1, 2));
    }

    @Test
    @DisplayName("Zombie stuck by walls and doors")
    public void testZombieStuck() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieTest_stuck", "c_movementTest_testMovementDown");


        // create the expected result
    
        assertEquals(getEntities(res, "zombie").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "zombie").get(0).getPosition(), new Position(1, 1));
    }
    @Test
    @DisplayName("Spider not affected by portals")
    public void testSpiderNotEffectedByPortals() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_spiderPortal", "c_movementTest_testMovementDown");
        
        //spider starts at (1,1), moves up, then right onto portal at (2,0)
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 0));
        res = dmc.tick(Direction.DOWN);
        // checks that spider is not portaled
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(2, 0));
    }

    @Test
    @DisplayName("Spider not affected by doors, switches, walls")
    public void testSpiderNotAffectedByStaticEntities() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_staticEntities", "c_movementTest_testMovementDown");
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(1, 0));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(2, 0));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "spider").get(0).getPosition(), new Position(2, 1));
    }
    @Test
    @DisplayName("merc bribe invalid id")
    public void testMercBribeInvalidId() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_spiderTest_staticEntities", "c_movementTest_testMovementDown");
        assertThrows(IllegalArgumentException.class, () -> {
            dmc.interact("badid");
        });
    }

    @Test
    @DisplayName("merc bribe too far")
    public void testMercBribeTooFar() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercTest_bribeTooFar", "c_movementTest_testMovementDown");
        assertThrows(InvalidActionException.class, () -> {
            dmc.interact(getEntities(res, "mercenary").get(0).getId());
        });
        

    }

    @Test
    @DisplayName("merc bribe not enough treasure")
    public void testMercNotEnoughTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercTest_bribeTooFar", "c_mercTests_bribeNotEnoughTreasure");
        assertThrows(InvalidActionException.class, () -> {
            dmc.interact(getEntities(res, "mercenary").get(0).getId());
        });
        

    }

    @Test
    @DisplayName("merc bribe")
    public void testMercBribe() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercTest_bribe", "c_mercTests_bribe");
        
        res = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(res, "mercenary").get(0).getPosition(), new Position(4, 0));
        EntityResponse initPlayer = getPlayer(res).get();
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 0),
                false);
                EntityResponse actualPlayer = getPlayer(res).get();
        assertEquals(expectedPlayer, actualPlayer);
        
        DungeonResponse res2 = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(res2, "mercenary").get(0).getPosition(), new Position(3, 0));
        initPlayer = getPlayer(res2).get();
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 0),
                false);
                actualPlayer = getPlayer(res2).get();
        assertEquals(expectedPlayer, actualPlayer);
        
        assertEquals(2, getInventory(res2, "treasure").size());
        assertDoesNotThrow(() -> {
            DungeonResponse resBribe = dmc.interact(getEntities(res2, "mercenary").get(0).getId());
            assertEquals(1, getInventory(resBribe, "treasure").size());
        });

        assertEquals(getEntities(res2, "mercenary").get(0).getPosition(), new Position(3, 0));
        initPlayer = getPlayer(res2).get();
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(2, 0),
                false);
                actualPlayer = getPlayer(res2).get();
        assertEquals(expectedPlayer, actualPlayer);

        DungeonResponse res3 = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(res3, "mercenary").get(0).getPosition(), new Position(2, 0));
        initPlayer = getPlayer(res3).get();
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 0),
                false);
                actualPlayer = getPlayer(res3).get();
        assertEquals(3, actualPlayer.getPosition().getX());
        assertEquals(0, actualPlayer.getPosition().getY());

        res3 = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(res3, "mercenary").get(0).getPosition(), new Position(3, 0));
        initPlayer = getPlayer(res3).get();
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(4, 0),
                false);
                actualPlayer = getPlayer(res3).get();
        assertEquals(expectedPlayer, actualPlayer);
    }
}
