package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getGoals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class GoalComplexTests {

    @Test
    @DisplayName("Test complex goal enemeies AND boulders")
    public void goalComplexEnemiesANDBoulders() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_EnemiesANDBoulders","c_complexGoalsTest_andAll");

        // Initial dungeon setup
        // (0, 0)PLAYER	  (1, 0)	     (2, 0)BOULDER	(3, 0)SWITCH
        // (0, 1)	      (1, 1)SPIDER	 (2, 1)    	    (3, 1)
        // Config file: "enemy_goal": 1, player attack/health >> spider attack/health

        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":boulders"));

        // player moves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":boulders"));

        // player moves right, pushes boulder onto switch, full goal complete
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal enemies OR boulders")
    public void goalComplexEnemiesORBoulders() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_EnemiesORBoulders","c_complexGoalsTest_andAll");

        // Initial dungeon setup
        // (0, 0)PLAYER	  (1, 0)	     (2, 0)BOULDER	(3, 0)SWITCH
        // (0, 1)	      (1, 1)SPIDER	 (2, 1)    	    (3, 1)
        // Config file: "enemy_goal": 1, player attack/health >> spider attack/health

        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":boulders"));

        // player moves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);

        // full goal complete
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal enemies AND treasure")
    public void goalComplexEnemiesANDTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_EnemiesANDTreasure","c_complexGoalsTest_andAll");

        // Initial dungeon setup
        // (0, 0)PLAYER	  (1, 0)	     (2, 0)TREASURE
        // (0, 1)	      (1, 1)SPIDER	 (2, 1)    	   
        // Config file: "enemy_goal": 1, player attack/health >> spider attack/health

        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":treasure"));

        // player momves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":treasure"));

        // player moves right, collects treasure, full goal complete
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal enemies OR treasure")
    public void goalComplexEnemiesORTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_EnemiesORTreasure","c_complexGoalsTest_andAll");

        // Initial dungeon setup
        // (0, 0)PLAYER	  (1, 0)	     (2, 0)TREASURE
        // (0, 1)	      (1, 1)SPIDER	 (2, 1)    	   
        // Config file: "enemy_goal": 1, player attack/health >> spider attack/health

        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":treasure"));

        // player moves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);

        // player moves right, collects treasure, full goal complete
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal enemies AND exit")
    public void goalComplexEnemiesANDExit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_EnemiesANDExit","c_complexGoalsTest_andAll");

        // Initial dungeon setup
        // (0, 0)MERCENARY	  (1, 0)PLAYER	 (2, 0)EXIT
        // (0, 1)	          (1, 1)	     (2, 1)    	   
        // Config file: "enemy_goal": 1, player attack/health >> mercenary attack/health

        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":exit"));

        // player moves right onto exit, goal unchanged
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":exit"));

        // player moves left, kills mercenary, mercenary subgoal achieved
        res = dmc.tick(Direction.LEFT);
        assertFalse(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":exit"));

        // player moves right onto exit, full goal complete
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal enemies OR exit")
    public void goalComplexEnemiesORExit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_EnemiesORExit","c_complexGoalsTest_andAll");

        // Initial dungeon setup
        // (0, 0)MERCENARY	  (1, 0)PLAYER	 (2, 0)EXIT
        // (0, 1)	          (1, 1)	     (2, 1)    	   
        // Config file: "enemy_goal": 1, player attack/health >> mercenary attack/health

        assertTrue(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":exit"));

        // player moves right onto exit, goal complete
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal: cover two switches with boulders AND collect two treasure")
    public void goalComplexBouldersANDTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_BouldersANDTreasure","c_goalComplex_treasureTwoEnemiesTwo");
  
        // Initial dungeon setup
        // (0, 0)PLAYER	   (1, 0)TREASURE	(2, 0)TREASURE	(3, 0)	        (4, 0)
        // (0, 1)	       (1, 1)	        (2, 1)BOULDER1	(3, 1)BOULDER2 	(4, 1)SWITCH
        // (0, 2)	       (1, 2)	        (2, 2)SWITCH	(3, 2)	        (4, 2)
        // Pick up two treasure for treasure goal

        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":treasure"));

        // pick up one treasure, treasure sub-goal not completed yet
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":treasure"));

        // pick up second treasure, treasure sub-goal completed
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":treasure"));

        // push boulder on first switch, boulders sub-goal not completed 
        res = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":treasure"));

        // push boulder on second switch, full goal complete
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal: cover two switches with boulders OR collect two treasure")
    public void goalComplexBouldersORTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_BouldersORTreasure","c_goalComplex_treasureTwoEnemiesTwo");

        // Initial dungeon setup
        // (0, 0)PLAYER	   (1, 0)TREASURE	(2, 0)TREASURE	(3, 0)	        (4, 0)
        // (0, 1)	       (1, 1)	        (2, 1)BOULDER1	(3, 1)BOULDER2 	(4, 1)SWITCH
        // (0, 2)	       (1, 2)	        (2, 2)SWITCH	(3, 2)	        (4, 2)
        // Pick up two treasure for treasure goal

        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":treasure"));

        // pick up one treasure, treasure sub-goal not completed yet
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":treasure"));

        // pick up second treasure, full goal completed
        res = dmc.tick(Direction.RIGHT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal: put boulder on switch AND get to exit")
    public void goalComplexBouldersANDExit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_BouldersANDExit","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	   (1, 0)       	(2, 0)	
        // (0, 1)EXIT	   (1, 1)BOULDER	(2, 1)SWITCH
        
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":exit"));
        
        // move player onto exit, neither sub-goal complete
        res = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":exit"));

        // move boulder onto switch, boulders sub-goal complete
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":exit"));

        // move player onto exit
        res = dmc.tick(Direction.LEFT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal put boulder on switch OR get to exit")
    public void goalComplexBouldersORExit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_BouldersORExit","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	   (1, 0)       	(2, 0)	
        // (0, 1)EXIT	   (1, 1)BOULDER	(2, 1)SWITCH
        
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":exit"));

        // move player onto exit, goal complete
        res = dmc.tick(Direction.DOWN);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal collect treasure AND get to exit")
    public void goalComplexTreasureANDExit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_TreasureANDExit","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	   (1, 0)       
        // (0, 1)EXIT	   (1, 1)TREASURE

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));

        // move player onto exit, no subgoal complete
        res = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));

        // Collect treasure, treasure subgoal complete
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));

        // Move player to exit, full goal complete
        res = dmc.tick(Direction.LEFT);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal collect treasure OR get to exit")
    public void goalComplexTreasureORExit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_TreasureORExit","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	   (1, 0)       
        // (0, 1)EXIT	   (1, 1)TREASURE

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));

        // move player onto exit, full goal complete
        res = dmc.tick(Direction.DOWN);
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal conjunction of treasure, exit, boulders and enemies")
    public void goalComplexANDTreasureExitBouldersEnemies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_Nested1","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	 (1, 0)     	 (2, 0)EXIT
        // (0, 1)    	 (1, 1)SPIDER    (2, 1)BOULDER    
        // (0, 2)    	 (1, 2)TREASURE  (2, 2)SWITCH  
        // Config file: "enemy_goal": 1, "treasure_goal": 1 

        // Goal: Treasure AND (Exit AND (Boulders AND Enemies))

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":enemies"));
        
        assertEquals("(:treasure AND (:exit AND (:boulders AND :enemies)))", getGoals(res));
    
        // player moves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);
        assertEquals("(:treasure AND (:exit AND (:boulders)))", getGoals(res));
        

        // player moves right onto exit, exit goal doesn't change
        res = dmc.tick(Direction.RIGHT);
        assertEquals("(:treasure AND (:exit AND (:boulders)))", getGoals(res));

        // player pushes boulder onto switch
        res = dmc.tick(Direction.DOWN);
        assertEquals("(:treasure AND (:exit))", getGoals(res));

        // player moves up onto exit, exit goal doesn't change
        res = dmc.tick(Direction.UP);
        assertEquals("(:treasure AND (:exit))", getGoals(res));

        // player collects treasure
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertEquals("((:exit))", getGoals(res));

        // player moves right onto exit, exit goal completes
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":exit"));

        // Full goal complete
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal conjunction of enemies, boulders, exit and treasure")
    public void goalComplexANDEnemiesBouldersExitTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_Nested2","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	 (1, 0)     	 (2, 0)EXIT
        // (0, 1)    	 (1, 1)SPIDER    (2, 1)BOULDER    
        // (0, 2)    	 (1, 2)TREASURE  (2, 2)SWITCH  
        // Config file: "enemy_goal": 1, "treasure_goal": 1 

        // Goal: Enemies AND (Boulders AND (Exit AND Treasure))

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":enemies"));

        // player moves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));

        // player moves right onto exit, exit goal doesn't change
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":exit"));

        // player pushes boulder onto switch
        res = dmc.tick(Direction.DOWN);
        assertFalse(getGoals(res).contains(":boulders"));

        // player moves up onto exit, exit goal doesn't change
        res = dmc.tick(Direction.UP);
        assertTrue(getGoals(res).contains(":exit"));

        // player collects treasure
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        assertFalse(getGoals(res).contains(":treasure"));

        // player moves right onto exit, exit goal completes
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":exit"));

        // Full goal complete
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal disjunction of treasure, exit, boulders and enemies")
    public void goalComplexORTreasureExitBouldersEnemies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_Nested3","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	 (1, 0)     	 (2, 0)EXIT
        // (0, 1)    	 (1, 1)SPIDER    (2, 1)BOULDER    
        // (0, 2)    	 (1, 2)TREASURE  (2, 2)SWITCH  
        // Config file: "enemy_goal": 1, "treasure_goal": 1 

        // Goal: Treasure OR (Exit OR (Boulders OR Enemies))

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":enemies"));
        System.out.println("goalString = "+dmc.getDungeon().getGoalString());

        // player moves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));
        assertFalse(getGoals(res).contains(":boulders"));

        // player moves right onto exit, exit goal doesn't change
        res = dmc.tick(Direction.RIGHT);

        // Full goal complete
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal disjunction of enemies, boulders, exit and treasure")
    public void goalComplexOREnemiesBouldersExitTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_Nested4","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	 (1, 0)     	 (2, 0)EXIT
        // (0, 1)    	 (1, 1)SPIDER    (2, 1)BOULDER    
        // (0, 2)    	 (1, 2)TREASURE  (2, 2)SWITCH  
        // Config file: "enemy_goal": 1, "treasure_goal": 1 

        // Goal: Enemies OR (Boulders OR (Exit OR Treasure))

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":enemies"));

        // player moves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));

        // Full goal complete
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test complex goal conjunction of treasure, exit, boulders and enemies")
    public void goalComplexANDORTreasureExitBouldersEnemies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_Nested5","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	 (1, 0)     	 (2, 0)EXIT
        // (0, 1)    	 (1, 1)SPIDER    (2, 1)BOULDER    
        // (0, 2)    	 (1, 2)TREASURE  (2, 2)SWITCH  
        // Config file: "enemy_goal": 1, "treasure_goal": 1 

        // Goal: Treasure OR (Exit AND (Boulders OR Enemies))

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":enemies"));

        // player moves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));
        assertFalse(getGoals(res).contains(":boulders"));

        // player moves right onto exit
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":exit"));

        // Full goal complete
        assertEquals("", getGoals(res));
    }   

    @Test
    @DisplayName("Test complex goal conjunction of treasure, exit, boulders and enemies")
    public void goalComplexORANDTreasureExitBouldersEnemies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_Nested6","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	 (1, 0)     	 (2, 0)EXIT
        // (0, 1)    	 (1, 1)SPIDER    (2, 1)BOULDER    
        // (0, 2)    	 (1, 2)TREASURE  (2, 2)SWITCH  
        // Config file: "enemy_goal": 1, "treasure_goal": 1 

        // Goal: Treasure AND (Exit OR (Boulders AND Enemies))

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":enemies"));

        // player moves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));
        assertTrue(getGoals(res).contains(":boulders"));
        // Goal: Treasure AND (Exit OR (Boulders))

        // player moves right onto exit, exit goal doesn't change
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":exit"));

        // player pushes boulder onto switch
        res = dmc.tick(Direction.DOWN);
        assertFalse(getGoals(res).contains(":boulders"));
        assertFalse(getGoals(res).contains(":exit"));

        // player collects treasure
        res = dmc.tick(Direction.LEFT);
        System.out.println("    goals="+dmc.getDungeon().getGoalString());
        res = dmc.tick(Direction.DOWN);
        System.out.println("    goals="+dmc.getDungeon().getGoalString());
        assertFalse(getGoals(res).contains(":treasure"));

        // Full goal complete
        assertEquals("", getGoals(res));
    }   

    @Test
    @DisplayName("Test complex goal conjunction of treasure, exit, boulders and enemies")
    public void goalComplexANDOREnemiesBouldersExitTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalComplex_Nested7","c_complexGoalsTest_andAll");
        
        // Initial dungeon setup
        // (0, 0)PLAYER	 (1, 0)     	 (2, 0)EXIT
        // (0, 1)    	 (1, 1)SPIDER    (2, 1)BOULDER    
        // (0, 2)    	 (1, 2)TREASURE  (2, 2)SWITCH  
        // Config file: "enemy_goal": 1, "treasure_goal": 1 

        // Goal: Enemies AND (Boulders OR (Exit AND Treasure))

        assertTrue(getGoals(res).contains(":treasure"));
        assertTrue(getGoals(res).contains(":exit"));
        assertTrue(getGoals(res).contains(":boulders"));
        assertTrue(getGoals(res).contains(":enemies"));

        // player moves right, spider moves up, player kills spider
        res = dmc.tick(Direction.RIGHT);
        assertFalse(getGoals(res).contains(":enemies"));

        // player moves right onto exit, exit goal doesn't change
        res = dmc.tick(Direction.RIGHT);
        assertTrue(getGoals(res).contains(":exit"));

        // player pushes boulder onto switch
        res = dmc.tick(Direction.DOWN);
        assertFalse(getGoals(res).contains(":boulders"));

        // Full goal complete
        assertEquals("", getGoals(res));
    }   

}