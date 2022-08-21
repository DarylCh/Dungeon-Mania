package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.Set;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class BossesTests {
    // assassin movement
    @Test
    @DisplayName("Tests that assassins moves towards the player")
    public void testBasicAssassinMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = assertDoesNotThrow(() -> {
            return dmc.newGame("d_assassinTest_movement",
            "c_movementTest_testMovementDown");
        });

        initRes = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(initRes, "assassin").size(), 2);
        assertEquals(new Position(9, 1), getEntities(initRes, "assassin").get(0).getPosition());
        assertEquals(new Position(0, 9), getEntities(initRes, "assassin").get(1).getPosition());
    }

    @Test
    @DisplayName("Tests that the assassin moves around walls/blocking objects")
    public void testBlockingMovementAssassin() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = assertDoesNotThrow(() -> {
            return dmc.newGame("d_assassinTest_blocking",
            "c_movementTest_testMovementDown");
        });

        initRes = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(initRes, "assassin").get(0).getPosition(), new Position(6, 1));
    }
    // assassin in battle
    @Test
    @DisplayName("simple battle with assassin")
    public void basicBattleTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_assassinTest_basicBattle",
                "c_battleTests_mercHealthConfig3");
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(res, "player").size(), 0);
    }

    @Test
    @DisplayName("assassin bribe not enough treasure") 
    public void testAssassinBribeNotEnoughTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_bribe", "c_mercTests_bribe3_bigradius");
        DungeonResponse res2 = dmc.tick(Direction.RIGHT);
        assertEquals(1, getInventory(res2, "treasure").size());
        assertThrows(InvalidActionException.class, () -> {
            DungeonResponse resBribe = dmc.interact(getEntities(res2, "assassin").get(0).getId());
        });
    }
    @Test
    @DisplayName("assassin bribe too far") 
    public void testAssassinBribeTooFar() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_bribe", "c_mercTests_bribe3");
        
        assertThrows(InvalidActionException.class, () -> {
            DungeonResponse resBribe = dmc.interact(getEntities(res, "assassin").get(0).getId());
        });
    }
    @Test
    @DisplayName("assassin bribe")
    public void testAssassinBribe() {
        Random rand = new Random(4);
        DungeonManiaController dmc = new DungeonManiaController(4);
        
        
        for (int i = 0; i < 100; i++) {
            DungeonResponse res = dmc.newGame("d_assassinTest_bribe", "c_mercTests_bribe3");
            res = dmc.tick(Direction.RIGHT);
            DungeonResponse res2 = dmc.tick(Direction.RIGHT);
        
            assertEquals(2, getInventory(res2, "treasure").size());
            assertDoesNotThrow(() -> {
                DungeonResponse resBribe = dmc.interact(getEntities(res2, "assassin").get(0).getId());
                // regardless of whether bribe failed, treasure is removed
                assertEquals(0, getInventory(resBribe, "treasure").size());
            });
            double bribeFailRate = Double.parseDouble(getValueFromConfigFile("assassin_bribe_fail_rate", "c_mercTests_bribe3"));
        
            if (rand.nextInt(100) <= 100*(1- bribeFailRate)) {
                System.out.println("success");
                DungeonResponse res3 = dmc.tick(Direction.RIGHT);
                assertEquals(getEntities(res3, "assassin").get(0).getPosition(), new Position(2, 0));
                assertEquals(getEntities(res3, "player").get(0).getPosition(), new Position(3, 0));

                res3 = dmc.tick(Direction.RIGHT);
                assertEquals(getEntities(res3, "assassin").get(0).getPosition(), new Position(3, 0));
                assertEquals(getEntities(res3, "player").get(0).getPosition(), new Position(4, 0));

                res3 = dmc.tick(Direction.LEFT);
                assertEquals(getEntities(res3, "assassin").get(0).getPosition(), new Position(4, 0));
                assertEquals(getEntities(res3, "player").get(0).getPosition(), new Position(3, 0));
            } else {
                // if bribe failed, the player died
                DungeonResponse res3 = dmc.tick(Direction.RIGHT);
                assertEquals(getEntities(res3, "player").size(), 0);
                assertEquals(getEntities(res3, "assassin").get(0).getPosition(), new Position(3, 0));
                
            }

        }
 
    }
   

    @Test
    @DisplayName("Assassin can see invisible player if within certain radius")
    public void testAssassinInvisiblePlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_assassinTest_seeInvisiblePlayer", "c_movement3");
        // create the expected result
        assertEquals(getEntities(initDungonRes, "assassin").get(0).getPosition(), new Position(6, 1));
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        // consume invisible potion
        assertEquals(getEntities(actualDungonRes, "assassin").get(0).getPosition(), new Position(5, 1));
        assertEquals(1, getInventory(actualDungonRes, "invisibility_potion").size());

        assertDoesNotThrow(() -> {
            DungeonResponse actualDungonRes2 = dmc.tick(getInventory(actualDungonRes, "invisibility_potion").get(0).getId());
            assertEquals(getEntities(actualDungonRes2, "assassin").get(0).getPosition(), new Position(4, 1));
        });
        DungeonResponse res = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(res, "assassin").get(0).getPosition(), new Position(3, 1));

    }
    @Test
    @DisplayName("Assassin can't see player when too far away")
    public void testAssassinCantSeeInvisiblePlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_assassinTest_seeInvisiblePlayer", "c_assassinSmallReconRadius");
        // create the expected result
        assertEquals(getEntities(initDungonRes, "assassin").get(0).getPosition(), new Position(6, 1));
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        // consume invisible potion
        assertEquals(getEntities(actualDungonRes, "assassin").get(0).getPosition(), new Position(5, 1));
        assertEquals(1, getInventory(actualDungonRes, "invisibility_potion").size());

        assertDoesNotThrow(() -> {
            DungeonResponse actualDungeonRes2 = dmc.tick(getInventory(actualDungonRes, "invisibility_potion").get(0).getId());
            Position finalPosition = getEntities(actualDungeonRes2, "assassin").get(0).getPosition();

            // zombie is in one of the cardinally adjacent positions
            Position final1 = new Position(4, 1);
            Position final2 = new Position(6, 1);
            Position final3 = new Position(5, 2);
            Position final4 = new Position(5, 0);
            assertTrue(Set.of(final1, final2, final3, final4).contains(finalPosition));
    
        });
        

    }
    

    // hydra movement
    @Test
    @DisplayName("hydra move simple")
    public void testMoveHydra() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraTest_simple", "c_movementTest_testMovementDown");


        // create the expected result
    
        assertEquals(getEntities(res, "hydra").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
        Position finalPosition = getEntities(res, "hydra").get(0).getPosition();

        // zombie is in one of the cardinally adjacent positions
        Position final1 = new Position(1, 0);
        Position final2 = new Position(1, 2);
        Position final3 = new Position(0, 1);
        Position final4 = new Position(2, 1);
        assertTrue(Set.of(final1, final2, final3, final4).contains(finalPosition));
        

    }
    @Test
    @DisplayName("Hydra move restricted by walls")
    public void testHydraMoveOnePossible() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res= dmc.newGame("d_hydraTest_onePossible", "c_movementTest_testMovementDown");
    
        assertEquals(getEntities(res, "hydra").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
       
        // position expected 1,0
        assertEquals(getEntities(res, "hydra").get(0).getPosition(), new Position(1, 2));
    }

    @Test
    @DisplayName("Hydra stuck by walls and doors")
    public void testHydraStuck() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_hydraTest_stuck", "c_movementTest_testMovementDown");


        // create the expected result
    
        assertEquals(getEntities(res, "hydra").get(0).getPosition(), new Position(1, 1));
        res = dmc.tick(Direction.DOWN);
        assertEquals(getEntities(res, "hydra").get(0).getPosition(), new Position(1, 1));
    }

    @Test
    @DisplayName("Hydra in battle")
    public void testHydraInBattle() {
        DungeonManiaController dmc = new DungeonManiaController(4);
        Random rand = new Random(4);
        for (int i = 0; i < 100; i++) {
            DungeonResponse res = dmc.newGame("d_hydraBattleTest", "c_movement3");
            res = dmc.tick(Direction.RIGHT);
            double hydraHealthIncreaseRate = Double.parseDouble(getValueFromConfigFile("hydra_health_increase_rate", "c_mercTests_bribe3"));
        
            if (rand.nextInt(100) <= 100*hydraHealthIncreaseRate) {
                // check player dies
                assertEquals(getEntities(res, "player").size(), 0);
                assertEquals(getEntities(res, "hydra").size(), 1);
            } else {
                assertEquals(getEntities(res, "hydra").size(), 0);
                assertEquals(getEntities(res, "player").size(), 1);
            }
                

        }
        
    }



    
}
