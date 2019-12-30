package kubys.Move;

import kubys.Application;
import kubys.Map.LandPlot;
import kubys.Player.Player;
import kubys.Player.Breed;
import kubys.Map.Command;
import kubys.Map.Position;
import kubys.Map.Map;
import kubys.Player.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@DisplayName("Test WebSocket STOMP client")
@AutoConfigureMockMvc
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerCommandTest {

    private Map mainMap;
    private Player player;
    private final Position position = Position.of(0, 1, 0);

    @Autowired
    PlayerCommandTest(Map mainMap) {
        this.mainMap = mainMap;
    }

    @BeforeEach
    void setup() {

        mainMap.getCells().clear();
        mainMap.generateEmptyMap(0, 1);

        player = Player.builder()
                .breed(Breed.DWARF)
                .level(1)
                .name("Joalien")
                .build();

        mainMap.addPlayer(player, position);

        assertEquals(position, Position.of(0, 1, 0));
    }

    @Test
    @DisplayName("move forward")
    void moveForward() {
        PlayerService.movePlayer(player, Command.FORWARD);
        assertEquals(player.getPosition(), position.plusZ(1));
    }

    @Test
    @DisplayName("climb landplot")
    void climbLandPlot() {
        Position landPlotPosition = position.plusZ(1);
        mainMap.getCells().put(landPlotPosition, LandPlot.builder().build());
        PlayerService.movePlayer(player, Command.FORWARD);
        assertEquals(player.getPosition(), position.plusZ(1).plusY(1));
    }

    @Test
    @DisplayName("Try to move forward and fail")
    void tryToMoveForward() {
        mainMap.getCells().put(position.plusZ(1), LandPlot.builder().build());
        mainMap.getCells().put(position.plusY(1).plusZ(1), LandPlot.builder().build());
        PlayerService.movePlayer(player, Command.FORWARD);
        assertEquals(player.getPosition(), position);
    }

    @Test
    @DisplayName("move forward with no landplot")
    void moveForwardThenFall() {
        mainMap.getCells().put(position.plusX(1).plusY(-2), LandPlot.builder().build());
        PlayerService.movePlayer(player, Command.RIGHT);
        assertEquals(player.getPosition(), position.plusX(1).plusY(-1));
    }
}
