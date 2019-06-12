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
    private Position position;
    private Position nextPosition;


    @BeforeEach
    void setup(){

        mainMap = new Map();

        player = Player.builder()
                .breed(Breed.DWARF)
                .level(1)
                .name("Joalien")
                .pa(10)
                .pm(5)
                .build();

        position = mainMap.addPlayer(player, Position.builder()
                .x(0)
                .y(1)
                .z(0)
                .build());

        nextPosition = Position.builder()
                .x(position.getX())
                .y(position.getY())
                .z(position.getZ()+1)
                .build();
        mainMap.generateEmptyMap(Position.builder().build());
    }

    @Test
    @DisplayName("move forward")
    void moveForward() {
        PlayerService.movePlayer(player, Command.FORWARD);
        assertEquals(player.getPosition(), nextPosition);
    }

    @Test
    @DisplayName("Try to move forward et fail")
    void tryToMoveForward() {
        mainMap.getCells().put(nextPosition, LandPlot.builder().build());
        PlayerService.movePlayer(player, Command.FORWARD);
        assertEquals(player.getPosition(), position);
    }
}
