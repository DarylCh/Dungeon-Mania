package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getEntities;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
public class LiveEntitySpawningTests {
    @Test
    @DisplayName("Test zombie is spawned in the right location after nth ticks")
    public void testSpawnZombie() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieTest_simpleSpawn", "c_spawnTests_spawnRate");
        
        // check no zombies for tick 0
        assertEquals(0, getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.LEFT);

        // check no zombies for tick 1
        assertEquals(0, getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.LEFT);

        // check no zombies for tick 2
        assertEquals(0, getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.LEFT);

        // check zombie created on tick 3
        assertEquals(1, getEntities(res, "zombie_toast").size());
        
        
        // Final position is one of the cardinally adjacent positions of the corresponding portal
        Position zombiePosition = getEntities(res, "zombie_toast").get(0).getPosition();
        Position spawnerPosition = getEntities(res, "zombie_toast_spawner").get(0).getPosition();
        int x = spawnerPosition.getX();
        int y =spawnerPosition.getY();
        Position final1 = new Position(x + 1, y);
        Position final2 = new Position(x - 1, y);
        Position final3 = new Position(x, y - 1);
        Position final4 = new Position(x, y + 1);
        assertTrue(Set.of(final1, final2, final3, final4).contains(zombiePosition));
        
    }

    // test no zombie spawn restrictions
    @Test
    @DisplayName("Test zombie does not spawn on top of wall, boulder or door")
    public void testSpawnZombieRestrictions() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieTest_spawnRestrictions", "c_spawnTests_spawnRate");
        
        // check no zombies for tick 0
        assertEquals(0, getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.LEFT);

        // check no zombies for tick 1
        assertEquals(0, getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.LEFT);

        // check no zombies for tick 2
        assertEquals(0, getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.LEFT);

        // check zombie created on tick 3
        assertEquals(1, getEntities(res, "zombie_toast").size());
        
        
        // Final position is one of the cardinally adjacent positions of the corresponding portal
        Position zombiePosition = getEntities(res, "zombie_toast").get(0).getPosition();
        Position spawnerPosition = getEntities(res, "zombie_toast_spawner").get(0).getPosition();
        int x = spawnerPosition.getX();
        int y =spawnerPosition.getY();
        Position final1 = new Position(x + 1, y);
        Position final2 = new Position(x - 1, y);
        Position final3 = new Position(x, y - 1);
        Position final4 = new Position(x, y + 1);
        assertTrue(Set.of(final1, final2, final3, final4).contains(zombiePosition));
        
    }
    // test spawn rate 0
    @Test
    @DisplayName("Test spider spawns after certain number of ticks, and exists on the map")
    public void testSpiderSpawn() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieTest_simpleSpawn", "c_spawnTests_spawnRate");
        
        // check no spider for tick 0
        assertEquals(0, getEntities(res, "spider").size());
        res = dmc.tick(Direction.LEFT);
        // check no spider for tick 1
        assertEquals(0, getEntities(res, "spider").size());
        res = dmc.tick(Direction.LEFT);
        // check no spider for tick 2
        assertEquals(0, getEntities(res, "spider").size());
        res = dmc.tick(Direction.LEFT);
        // check no spider for tick 3
        assertEquals(0, getEntities(res, "spider").size());
        res = dmc.tick(Direction.LEFT);
        // check no spider for tick 4
        assertEquals(0, getEntities(res, "spider").size());
        res = dmc.tick(Direction.LEFT);

        assertEquals(1, getEntities(res, "spider").size());
        
    }
    // test spawn rate 0
    @Test
    @DisplayName("Test no zombies spawned if spawn_rate = 0")
    public void testNoZombieSpawn() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieTest_simpleSpawn", "c_battleTests_basicBattleConfig");
        // check that no zombie spawned after 100 ticks
        for (int i = 0; i < 100; i++) {
            assertEquals(0, getEntities(res, "zombie_toast").size());
            res = dmc.tick(Direction.LEFT);
        }
        
    }

    @Test
    @DisplayName("Test no spiders spawned if spawn_rate = 0")
    public void testNoSpiderSpawn() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieTest_simpleSpawn", "c_battleTests_basicBattleConfig");
        // check that no spider spawned after 100 ticks
        for (int i = 0; i < 100; i++) {
            assertEquals(0, getEntities(res, "spider").size());
            res = dmc.tick(Direction.LEFT);
        }
        
    }

}
