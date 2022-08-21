package dungeonmania;
import static dungeonmania.TestUtils.getEntities;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MercMovementTests {
    @Test
    @DisplayName("Tests that the merc moves towards the player")
    public void testBasicMercMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = assertDoesNotThrow(() -> {
            return dmc.newGame("d_movementTest_basicMercenary",
            "c_movementTest_testMovementDown");
        });

        initRes = dmc.tick(Direction.RIGHT);
        List<EntityResponse> entities = getEntities(initRes, "mercenary");
        assertEquals(entities.size(), 2);
        assertEquals(new Position(9, 1), entities.get(0).getPosition());
        assertEquals(new Position(0, 9), entities.get(1).getPosition());
    }

    @Test
    @DisplayName("Tests that the merc moves around walls/blocking objects")
    public void testBlockingMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = assertDoesNotThrow(() -> {
            return dmc.newGame("d_movementTest_basicBlockingMercenary",
            "c_movementTest_testMovementDown");
        });

        initRes = dmc.tick(Direction.RIGHT);
        List<EntityResponse> entities = getEntities(initRes, "mercenary");
        assertEquals(entities.get(0).getPosition(), new Position(6, 1));
    }
    @Test
    @DisplayName("Tests that the merc can be blocked")
    public void testBlockedMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initRes = assertDoesNotThrow(() -> {
            return dmc.newGame("d_movementTest_mercBlocked",
            "c_movementTest_testMovementDown");
        });

        initRes = dmc.tick(Direction.RIGHT);
        List<EntityResponse> entities = getEntities(initRes, "mercenary");
        assertEquals(entities.get(0).getPosition(), new Position(5, 1));
    }
}
