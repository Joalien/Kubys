package kubys.Move;

import kubys.map.LandPlot;
import kubys.map.Map;
import kubys.map.MapService;
import kubys.map.Position;
import kubys.player.Command;
import kubys.player.Player;
import kubys.player.PlayerService;
import kubys.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        void moveForward() {
            mainMap.getCells().put(position.plusY(-1).plusZ(1), LandPlot.builder().build());
            playerService.movePlayer(player, Command.FORWARD);
            assertEquals(player.getPosition(), position.plusZ(1));
        }

        @Test
        void failMovingForward() {
            mainMap.getCells().put(position.plusZ(1), LandPlot.builder().build());
            mainMap.getCells().put(position.plusY(1).plusZ(1), LandPlot.builder().build());
            playerService.movePlayer(player, Command.FORWARD);
            assertEquals(player.getPosition(), position);
        }

        @Test
        void climbLandPlot() {
            mainMap.getCells().put(position.plusZ(1), LandPlot.builder().build());
            playerService.movePlayer(player, Command.FORWARD);
            assertEquals(player.getPosition(), position.plusZ(1).plusY(1));
        }

        @Test
        void failClimbingLandPlot() {
            mainMap.getCells().put(position.plusZ(1), LandPlot.builder().build());
            mainMap.getCells().put(position.plusY(1), LandPlot.builder().build());
            playerService.movePlayer(player, Command.FORWARD);
            assertEquals(player.getPosition(), position);
        }

        @Test
        void moveForwardThenFall() {
            mainMap.getCells().put(position.plusX(1).plusY(-2), LandPlot.builder().build());
            playerService.movePlayer(player, Command.RIGHT);
            assertEquals(player.getPosition(), position.plusX(1).plusY(-1));
        }

        @Test
        void failMovingThenFalling() {
            // TODO: Find workaround
            // Warning, it will not stop infinite loop, but at least the test will not pass
            assertTimeout(ofMillis(1000), () -> {
                playerService.movePlayer(player, Command.FORWARD);
                assertEquals(player.getPosition(), position);
            });
        }

        @AfterEach
        void testPlayerPositionIsTheSameThatItsPositionInsideMap() {
            assertTrue(isPlayerInsideMap(mainMap, player));
        }
    }

    @Nested
    class switchTowardFightMapTest {
        Map otherMap;

        @BeforeEach
        void setup() {
            otherMap = MapService.generateFightMap();
            playerService.switchMap(player, otherMap);
        }

        @Test
        void changeUserMap() {
            assertEquals(player.getMap(), otherMap);
            assertTrue(otherMap.getCells().values().stream().anyMatch(cell -> cell.equals(player)));
            assertFalse(mainMap.getCells().values().stream().anyMatch(cell -> cell.equals(player)));
        }

        @Test
        void verifyThatUserStartsOnStartingCell() {
            assertTrue(otherMap.getStartingPositions().contains(player.getPosition()));
        }

        @AfterEach
        void testPlayerPositionIsTheSameThatItsPositionInsideMap() {
            assertTrue(isPlayerInsideMap(otherMap, player));
        }
    }

    static boolean isPlayerInsideMap(Map map, Player player) {
        return map.getCells().get(player.getPosition()).equals(player);
    }
}
