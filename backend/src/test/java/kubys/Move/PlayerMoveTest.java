package kubys.Move;

import kubys.Application;
import kubys.model.*;
import kubys.model.common.Breed;
import kubys.model.common.Move;
import kubys.model.common.Position;
import kubys.model.Map;
import kubys.service.PlayerService;
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
class PlayerMoveTest {

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
        log.info(player.getPosition().toString());
        PlayerService.movePlayer(player, Move.FORWARD);
        assertEquals(player.getPosition(), nextPosition);
        log.info(player.getPosition().toString());
    }

    @Test
    @DisplayName("Try to move forward et fail")
    void tryToMoveForward() {
        log.info(player.getPosition().toString());
        mainMap.getCells().put(nextPosition, LandPlot.builder().build());
        PlayerService.movePlayer(player, Move.FORWARD);
        assertEquals(player.getPosition(), position);
        log.info(player.getPosition().toString());
    }
}
