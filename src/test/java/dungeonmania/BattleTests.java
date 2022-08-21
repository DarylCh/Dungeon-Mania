package dungeonmania;

import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getValueFromConfigFile;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;



public class BattleTests {
    @Test
    @DisplayName("Tests that combat is successfully initiated")
    public void basicBattleTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("d_basicBattleTest_Merc",
                "c_battleTests_mercHealthConfig");
        dmc.tick(Direction.RIGHT);
    }

    @Test
    @DisplayName("Tests that player can use weapons")
    public void weaponBattleTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicBattleTest_Merc2",
                "c_battleTests_mercHealthConfig");


        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, getEntities(res, "mercenary").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, getEntities(res, "mercenary").size());
    }

     
    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies, String configFilePath, int numSwords, int numShields, int numBows, int numAllies, int numArmour) {
    List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType + "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType + "_attack", configFilePath));
        double bowAttack = Math.pow(2, numBows);
        double allyAttack = Double.parseDouble(getValueFromConfigFile("ally_attack", configFilePath))*numAllies;
        double allyDefence = Double.parseDouble(getValueFromConfigFile("ally_defence", configFilePath))*numAllies;
        double armourAttack = 0;
        double armourDefence = 0;
        if (getValueFromConfigFile("midnight_armour_attack", configFilePath) != null) {
            System.out.println("done");
            armourAttack = Double.parseDouble(getValueFromConfigFile("midnight_armour_attack", configFilePath))*numArmour;
            armourDefence = Double.parseDouble(getValueFromConfigFile("midnight_armour_defence", configFilePath))*numArmour;
        }

        double swordAttack = numSwords*Double.parseDouble(getValueFromConfigFile("sword_attack", configFilePath));
        double shieldDefence = numShields*Double.parseDouble(getValueFromConfigFile("shield_defence", configFilePath));
        for (RoundResponse round : rounds) {
            assertEquals(-((enemyAttack - shieldDefence - allyDefence - armourDefence)/ 10), round.getDeltaCharacterHealth(), 0.001);
            assertEquals(-((bowAttack * (playerAttack + swordAttack + allyAttack + armourAttack))/ 5), round.getDeltaEnemyHealth(), 0.001);
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }
    private static DungeonResponse genericMercenarySequence(DungeonManiaController controller, String configFile) {
        /*
         *  exit   wall  wall  wall
         * player  [  ]  merc  wall
         *  wall   wall  wall  wall
         */
        DungeonResponse initialResponse = controller.newGame("d_battleTest_basicMercenary", configFile);
        int mercenaryCount = countEntityOfType(initialResponse, "mercenary");
        assertEquals(1, countEntityOfType(initialResponse, "player"));
        assertEquals(1, mercenaryCount);
        return controller.tick(Direction.RIGHT);
    }
    
    @Test
    @DisplayName("Test battle with all weapons")
    public void simpleBattleAllWeapons() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_buildables",
        "c_battleTests_basicBattleConfig");
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        

        res = assertDoesNotThrow(() -> dmc.build("shield"));
        
        assertDoesNotThrow(() -> dmc.build("bow"));

        res = dmc.tick(Direction.RIGHT);
        
        assertEquals(getInventory(res, "shield").size(), 1);
        assertEquals(getInventory(res, "bow").size(), 1);
        assertEquals(getInventory(res, "sword").size(), 1);
        // battle
        res = dmc.tick(Direction.RIGHT);
        BattleResponse battle = res.getBattles().get(0);
        assertBattleCalculations("mercenary", battle, true,
        "c_battleTests_basicBattleConfig", 1,1,1,0,0);
        // Check that all of the weapons have durability 0 after battle ends
        assertEquals(0, getInventory(res, "sword").size());
        assertEquals(0, getInventory(res, "shield").size());
        assertEquals(0, getInventory(res, "bow").size());

    }

    @Test
    @DisplayName("Test battle with midnight armour")
    public void midnightArmourBattle() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmour_battle",
        "c_battleTests_midnightArmour");
       // pick up 1 x sword
       res = dmc.tick(Direction.RIGHT);
    
       assertEquals(1, getInventory(res, "sword").size());
       // pick up 1 x sunstone
       res = dmc.tick(Direction.RIGHT);
       assertEquals(1, getInventory(res, "sun_stone").size());

        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(getInventory(res, "midnight_armour").size(), 1);
        
        // battle
        res = dmc.tick(Direction.RIGHT);
        BattleResponse battle = res.getBattles().get(0);
        assertBattleCalculations("mercenary", battle, true,
        "c_battleTests_midnightArmour", 0,0,0,0, 1);
        
    }

    @Test
    @DisplayName("Test weapon durability in battle")
    public void weaponDurabilityInMultiBattle() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_durability",
        "c_battleTests_durabilityConfig");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(getInventory(res, "sword").size(), 1);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getBattles().size(), 1);
        assertEquals(getInventory(res, "sword").size(), 1);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(res.getBattles().size(), 2);
        assertEquals(getInventory(res, "sword").size(), 0);
    }

    @Test
    @DisplayName("Test simultaneous battles on same tick")
    public void multiBattleInSameTick() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_multiBattle",
        "c_battleTests_durabilityConfig");
        res = dmc.tick(Direction.DOWN);
        // two battles, player dies in the second
        assertEquals(res.getBattles().size(), 2);
        assertEquals(getEntities(res, "mercenary").size(), 1);
        assertEquals(getEntities(res, "player").size(), 0);
    }

    @Test
    @DisplayName("Test basic battle calculations - mercenary - player loses")
    public void testHealthBelowZeroMercenary() {
       DungeonManiaController controller = new DungeonManiaController();
       DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_basicMercenaryPlayerDies");
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("mercenary", battle, false, "c_battleTests_basicMercenaryPlayerDies", 0, 0, 0, 0, 0);
       assertEquals(0, getEntities(postBattleResponse, "player").size());
    }

    @Test
    @DisplayName("Test basic battle calculations - mercenary - player wins")
    public void testRoundCalculationsMercenary() {
       DungeonManiaController controller = new DungeonManiaController();
       DungeonResponse postBattleResponse = genericMercenarySequence(controller, "c_battleTests_basicMercenaryMercenaryDies");
       BattleResponse battle = postBattleResponse.getBattles().get(0);
       assertBattleCalculations("mercenary", battle, true, "c_battleTests_basicMercenaryMercenaryDies", 0, 0, 0, 0, 0);
       assertEquals(1, getEntities(postBattleResponse, "player").size());
    }



    // Potion effect on battle
    @Test
    @DisplayName("Test that no battles occur when invisibility potion is in action")
    public void testInvisibleBattle() {
        DungeonManiaController dmc = new DungeonManiaController();
       
        DungeonResponse res = dmc.newGame("d_battleTest_invisiblePotion", "c_potionTests_longPotions");
        // pick up potion
        res = dmc.tick(Direction.RIGHT);
        String id = getInventory(res, "invisibility_potion").get(0).getId();
        assertDoesNotThrow(() ->dmc.tick(id));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
       
    }

    @Test
    @DisplayName("Test that battle ends immediately when invincibility potion is in action")
    public void testInvincibleBattle() {
        DungeonManiaController dmc = new DungeonManiaController();
       // Initial dungeon setup
        // empty (0, 0) | empty (1,0)| WALL (2, 0) | WALL (3, 0) | WALL (4, 0)| WALL (5, 0)
        // empty (0, 1)|PLAYER (1, 1)| POTION (2, 1) | empty (3, 1) | merc (4, 1) | WALL (5, 1)
        // WALL (0, 2) | empty (1, 2) | WALL (2, 2) | WALL (3, 2) | WALL (4, 2)| WALL (5, 2)
        DungeonResponse res = dmc.newGame("d_battleTest_invinciblePotion", "c_potionTests_longPotions");
        // pick up potion
        res = dmc.tick(Direction.RIGHT);
        String id = getInventory(res, "invincibility_potion").get(0).getId();
        assertEquals(getEntities(res, "mercenary").get(0).getPosition(),new Position (3,1));
        // use potion. merc moves away from player in this same tick, so no battle
        res = assertDoesNotThrow(() ->dmc.tick(id));
        assertEquals(getEntities(res, "mercenary").get(0).getPosition(),(new Position (4,1)));
        assertEquals(0, res.getBattles().size());
        
        // player is at (2,1), merc should stay still at (4,1)
        res = dmc.tick(Direction.RIGHT);
        assertEquals(getEntities(res, "mercenary").get(0).getPosition(),(new Position (4,1)));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());

        // check only one round
        assertEquals(res.getBattles().get(0).getRounds().size(), 1);
        // check merc dies
        assertEquals(getEntities(res, "mercenary").size(), 0);   
        
        // check that potion is added to weaponry used
        assertEquals(res.getBattles().get(0).getRounds().get(0).getWeaponryUsed().get(0).getType(), "invincibility_potion");
    }

    @Test
    @DisplayName("Test for allies player dead")
    public void allyInBattleTestDies() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_allyBattleTest", "c_allyBattleTest_spiderW");

        controller.tick(Direction.LEFT);

        DungeonResponse postBattleResponse = controller.tick(Direction.LEFT);

        postBattleResponse.getBattles().get(0);
        assertEquals(0, getEntities(postBattleResponse, "player").size());
    }

    @Test
    @DisplayName("Test for allies player win on defence")
    public void allyInBattleTestWDefence() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_allyBattleTest", "c_allyBattleTest_pWD");

        DungeonResponse res2 = controller.tick(Direction.LEFT);

        assertDoesNotThrow(() -> {
            controller.interact(getEntities(res2, "mercenary").get(0).getId());
        });

        DungeonResponse postBattleResponse = controller.tick(Direction.LEFT);

        postBattleResponse.getBattles().get(0);
        assertEquals(1, getEntities(postBattleResponse, "player").size());
    }

    @Test
    @DisplayName("Test for allies player win on attack")
    public void allyInBattleTestAttack() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("d_allyBattleTest", "c_allyBattleTest_pWD");

        DungeonResponse res2 = controller.tick(Direction.LEFT);

        assertDoesNotThrow(() -> {
            controller.interact(getEntities(res2, "mercenary").get(0).getId());
        });

        DungeonResponse postBattleResponse = controller.tick(Direction.LEFT);

        postBattleResponse.getBattles().get(0);
        //assertBattleCalculations("spider", battle, true, "c_allyBattleTest_pWD", 0, 0, 0);
        assertEquals(1, getEntities(postBattleResponse, "player").size());
    }

    @Test
    @DisplayName("Test for allies")
    public void allyInBattleTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_battleTest_allyMerc", "c_mercTests_bribe");
        
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
        
        // bribing 
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
        // check that battle has occurred and that one merc has died
        assertEquals(res3.getBattles().size(), 1);
        BattleResponse battle = res3.getBattles().get(0);
        assertBattleCalculations("mercenary", battle, true, "c_mercTests_bribe", 0, 0,0, 1, 0);
        assertEquals(getEntities(res3, "mercenary").size(), 1);

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
