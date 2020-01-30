package kubys.Move;

import kubys.Map.MapService;
import kubys.Map.Model.LandPlot;
import kubys.Map.Model.Map;
import kubys.Map.Position;
import kubys.Player.Breed;
import kubys.Player.Command;
import kubys.Player.Player;
import kubys.Player.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@SpringBootTest
@DisplayName("Test user displacement")
//@AutoConfigureMockMvc
@Slf4j
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerCommandTest {

    private Map mainMap;
    private Player player;
    private Position position;

    @BeforeEach
    void setup() {
        this.mainMap = MapService.MAIN_MAP;
        mainMap.getCells().clear();
        position = Position.of(0, 1, 0);

        player = Player.builder()
                .breed(Breed.DWARF)
                .level(1)
                .name("Joalien")
                .map(mainMap)
                .position(position)
                .build();

        mainMap.getCells().put(position, player);

        assertEquals(position, Position.of(0, 1, 0));
    }

    @Test
    @DisplayName("move forward")
    void moveForward() {
        mainMap.getCells().put(position.plusY(-1).plusZ(1), LandPlot.builder().build());
        PlayerService.movePlayer(player, Command.FORWARD);
        assertEquals(player.getPosition(), position.plusZ(1));
    }

    @Test
    @DisplayName("climb landplot")
    void climbLandPlot() {
        mainMap.getCells().put(position.plusZ(1), LandPlot.builder().build());
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
    @DisplayName("move forward and drop")
    void moveForwardThenFall() {
        mainMap.getCells().put(position.plusX(1).plusY(-2), LandPlot.builder().build());
        PlayerService.movePlayer(player, Command.RIGHT);
        assertEquals(player.getPosition(), position.plusX(1).plusY(-1));
    }

    @Test
    @DisplayName("no infinite loop if user fall")
    void moveThenDontFall() {
        // TODO: Find waorkaround
        //  Warning, it will not stop infinite loop, but at least the test will not pass
        assertTimeout(ofMillis(1000), () -> {
            PlayerService.movePlayer(player, Command.FORWARD);
            assertEquals(player.getPosition(), position);
        });
    }
}
