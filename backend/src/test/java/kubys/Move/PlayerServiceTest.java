package kubys.Move;

import kubys.Map.LandPlot;
import kubys.Map.Map;
import kubys.Map.MapService;
import kubys.Map.Position;
import kubys.Player.Command;
import kubys.Player.Player;
import kubys.Player.PlayerService;
import kubys.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Test user displacement")
@Slf4j
class PlayerServiceTest {

    private Map mainMap;
    private Player player;
    private Position position;
    @Autowired
    private PlayerService playerService;

    @BeforeEach
    void setup() {
        mainMap = MapService.MAIN_MAP;
        mainMap.getCells().clear();
        position = Position.of(0, 1, 0);

        player = TestHelper.createRandomNewPlayer();
        player.setMap(mainMap);
        player.setPosition(position);

        mainMap.getCells().put(position, player);

        assertNotNull(mainMap);
        assertNotNull(position);
        assertNotNull(player);
        assertEquals(position, Position.of(0, 1, 0));
    }

    @Nested
    class movePlayerTest {

        @Test
        @DisplayName("test CREATE command")
        void createUser() {
            // TODO
        }

        @Test
        @DisplayName("move forward")
        void moveForward() {
            mainMap.getCells().put(position.plusY(-1).plusZ(1), LandPlot.builder().build());
            playerService.movePlayer(player, Command.FORWARD);
            assertEquals(player.getPosition(), position.plusZ(1));
        }

        @Test
        @DisplayName("climb landplot")
        void climbLandPlot() {
            mainMap.getCells().put(position.plusZ(1), LandPlot.builder().build());
            playerService.movePlayer(player, Command.FORWARD);
            assertEquals(player.getPosition(), position.plusZ(1).plusY(1));
        }

        @Test
        @DisplayName("can't move forward")
        void tryToMoveForward() {
            mainMap.getCells().put(position.plusZ(1), LandPlot.builder().build());
            mainMap.getCells().put(position.plusY(1).plusZ(1), LandPlot.builder().build());
            playerService.movePlayer(player, Command.FORWARD);
            assertEquals(player.getPosition(), position);
        }

        @Test
        @DisplayName("move forward and drop")
        void moveForwardThenFall() {
            mainMap.getCells().put(position.plusX(1).plusY(-2), LandPlot.builder().build());
            playerService.movePlayer(player, Command.RIGHT);
            assertEquals(player.getPosition(), position.plusX(1).plusY(-1));
        }

        @Test
        @DisplayName("no infinite loop if user fall")
        void moveThenDontFall() {
            // TODO: Find waorkaround
            //  Warning, it will not stop infinite loop, but at least the test will not pass
            assertTimeout(ofMillis(1000), () -> {
                playerService.movePlayer(player, Command.FORWARD);
                assertEquals(player.getPosition(), position);
            });
        }

        @AfterEach
        @DisplayName("implicit test that player's position is the same that its map position")
        void userPosition() {
            assertEquals(mainMap.getCells().get(player.getPosition()), player);
        }
    }

    @Nested
    class switchMapTest {
        Map otherMap;

        @BeforeEach
        void setup() {
            otherMap = MapService.generateFightMap();
            playerService.switchMap(player, otherMap);
        }

        @Test
        @DisplayName("player is only on the new map")
        void changeUserMap() {
            assertEquals(player.getMap(), otherMap);
            assertTrue(otherMap.getCells().values().stream().anyMatch(cell -> cell.equals(player)));
            assertFalse(mainMap.getCells().values().stream().anyMatch(cell -> cell.equals(player)));
        }

        @Test
        @DisplayName("user starts on starting cell")
        void userStartsOnStartingCell() {
            assertTrue(otherMap.getStartingPositions().contains(player.getPosition()));
        }

        @AfterEach
        @DisplayName("implicit test that player's position is the same that its map position")
        void userPosition() {
            assertEquals(otherMap.getCells().get(player.getPosition()), player);
        }
    }
}
