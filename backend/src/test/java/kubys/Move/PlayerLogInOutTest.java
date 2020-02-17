package kubys.Move;

import kubys.TestHelper;
import kubys.map.MapService;
import kubys.map.Position;
import kubys.player.Command;
import kubys.player.Player;
import kubys.player.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayName("Test user displacement")
@Slf4j
class PlayerLogInOutTest {
    @Autowired
    private PlayerService playerService;

    @BeforeEach
    void clearMainMap() {
        MapService.MAIN_MAP.getCells().clear();
    }

    @Test
    void createUser() { // This test might fail if we decide to kill people who fall
        Player player = TestHelper.createRandomNewPlayer();
        playerService.movePlayer(player, Command.CREATE);

        assertNotNull(player.getPosition());
        assertNotNull(player.getMap());
        assertTrue(MapService.MAIN_MAP.getStartingPositions().contains(player.getPosition()));
        assertTrue(player.isConnected());
    }

    @Test
    void removeUser() {
        Position position = Position.of(0, 1, 0);
        Player player = TestHelper.createRandomNewPlayer();
        player.setMap(MapService.MAIN_MAP);
        player.setPosition(position);
        MapService.MAIN_MAP.getCells().put(position, player);
        assertEquals(MapService.MAIN_MAP.getCells().get(player.getPosition()), player);
        assertTrue(player.isConnected());

        playerService.movePlayer(player,Command.REMOVE);

        assertTrue(MapService.MAIN_MAP.getCells().isEmpty());
        assertFalse(player.isConnected());
    }
}
