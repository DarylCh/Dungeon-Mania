package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import java.lang.IllegalArgumentException;
    
    
public class GameStartupTests {
    @Test
    @DisplayName("Tests that the game can be created successfully")
    public void basicTest() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> {
            dmc.newGame("Non_existant_path", "c_movementTest_testMovementDown");
            dmc.newGame("d_movementTest_testMovementDown", "Non_existant_path");
            
            dmc.newGame("Non_existant_path", "simple");
            dmc.newGame("zombies", "non_existant_path");

            // Setups should both come from either the test or main folder only
            dmc.newGame("d_movementTest_testMovementDown", "simple");
            dmc.newGame("zombies", "c_movementTest_testMovementDown");
            
            dmc.newGame("Non_existant_path", "Non_existant_path");
        });
        
        assertDoesNotThrow(() -> {
            DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown", "c_M3_config");
            System.out.println(initDungonRes);
        });

        assertDoesNotThrow(() -> {
            DungeonResponse initDungonRes = dmc.newGame("d_bombTest_placeBombRadius2", "c_bombTest_placeBombRadius2");
            System.out.println(initDungonRes);
        });
    }

}
