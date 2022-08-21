package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getGoals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class GoalSimpleTests {

    @Test
    @DisplayName("Test simple enemies goal. Kill two enemies (spider and mercenary).")
    public void goalSimpleEnemies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalSimple_enemies","c_goalComplex_treasureTwoEnemiesTwo");

        assertTrue(getGoals(res).contains(":enemies"));
        
        // Initial dungeon setup
        // (0, 0)MERCENARY	(1, 0)PLAYER    (2, 0)
        // (0, 1)	        (1, 1)	        (2, 1)SPIDER	
        // config: "enemy_goal": 2, "spider_health": 1, "mercenary_health": 1
        // player attack and health >> spider, mercernary attack and health

        assertEquals(getEntities(res, "spider").size(), 1);
        assertEquals(getEntities(res, "mercenary").size(), 1);
        assertEquals(dmc.getDungeon().getPlayer().getEnemyKillCount(), 0); 
        

        // player moves right and spider moves up, player kills spider, mercernary follows player, goal is not yet achieved
        res = dmc.tick(Direction.RIGHT);
        
        assertEquals(dmc.getDungeon().getPlayer().getEnemyKillCount(), 1); 
        assertEquals(getEntities(res, "spider").size(), 0);
        
        assertEquals(getEntities(res, "mercenary").size(), 1);
        Position posMercenary = getEntities(res, "mercenary").get(0).getPosition();
        assertTrue(posMercenary.equals( new Position(1, 0) ));

        assertTrue(getGoals(res).contains(":enemies"));

        // player moves left, player kills mercenary
        res = dmc.tick(Direction.LEFT);

        assertEquals(0, getEntities(res, "mercenary").size());
        assertEquals(2, dmc.getDungeon().getPlayer().getEnemyKillCount()); 
        
        // goal successfully achieved
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test simple boulder goal. Put boulders on all four floor switches.")
    public void goalSimpleBoulder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalSimple_bouldersFourSwitches","c_complexGoalsTest_andAll");
        
        assertTrue(getGoals(res).contains(":boulders"));

        // Initial dungeon setup
        // (0, 0)	        (1, 0)	          (2, 0)SWITCH	    (3, 0)	        (4, 0)	        (5, 0)
        // (0, 1)	        (1, 1)	          (2, 1)BOULDER1	(3, 1)	        (4, 1)	        (5, 1)BOULDER
        // (0, 2)SWITCH	    (1, 2)BOULDER4    (2, 2)PLAYER	    (3, 2)BOULDER2	(4, 2)SWITCH	(5, 2)BOULDER    <-- unused boulders
        // (0, 3)	        (1, 3)	          (2, 3)BOULDER3	(3, 3)	        (4, 3)	        (5, 3)BOULDER
        // (0, 4)	        (1, 4)	          (2, 4)SWITCH	    (3, 4)	        (4, 4)	        (5, 4)

        // move boulders onto switches
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        Position posBoulder1 = getEntities(res, "boulder").get(1).getPosition();
        assertTrue(posBoulder1.equals( new Position(2, 0) ));
        assertEquals(":boulders", getGoals(res));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        Position posBoulder2 = getEntities(res, "boulder").get(3).getPosition();
        assertTrue(posBoulder2.equals( new Position(4, 2) ));
        assertEquals(":boulders", getGoals(res));

        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        Position posBoulder3 = getEntities(res, "boulder").get(2).getPosition();
        assertTrue(posBoulder3.equals( new Position(2, 4) ));
        assertEquals(":boulders", getGoals(res));

        res = dmc.tick(Direction.LEFT);
        Position posBoulder4 = getEntities(res, "boulder").get(0).getPosition();
        assertTrue(posBoulder4.equals( new Position(0, 2) ));

        // goal successfully achieved
        assertEquals("", getGoals(res));

        // take a boulder off one of the floor switches
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        posBoulder4 = getEntities(res, "boulder").get(0).getPosition();
        assertTrue(posBoulder4.equals( new Position(0, 1) ));

        // goal has been un-achived
        assertEquals(":boulders", getGoals(res));

        // put the boulder back on
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        posBoulder4 = getEntities(res, "boulder").get(0).getPosition();
        assertTrue(posBoulder4.equals( new Position(0, 2) ));

        // goal successfully re-achieved
        assertEquals("", getGoals(res));
    }

    @Test
    @DisplayName("Test simple treasure goal. Collect four of the seven treasures.")
    public void goalSimpleTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalSimple_treasure","c_simpleGoal_fourTreasure");
        
        assertTrue(getGoals(res).contains(":treasure"));

        // Initial dungeon setup
        // (0, 0)	        (1, 0)	        (2, 0)      	(3, 0)	        (4, 0)	        (5, 0)
        // (0, 1)	        (1, 1)	        (2, 1)TREASURE	(3, 1)	        (4, 1)	        (5, 1)TREASURE
        // (0, 2)      	    (1, 2)TREASURE	(2, 2)PLAYER	(3, 2)TREASURE	(4, 2)      	(5, 2)TREASURE    <-- unused treasure
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

    @Test
    @DisplayName("Test simple exit goal. Move onto the exit.")
    public void goalSimpleExit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_goalSimple_exit","c_simpleGoal_fourTreasure");
        
        assertTrue(getGoals(res).contains(":exit"));

        // Initial dungeon setup
        // (0, 0)PLAYER	    (1, 0)EXIT

        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        assertEquals(":exit", getGoals(res));

        // move onto exit
        res = dmc.tick(Direction.RIGHT);

        // goal successfully achieved
        assertEquals("", getGoals(res));
    }
}