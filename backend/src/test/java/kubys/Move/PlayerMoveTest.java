package kubys.Move;

import kubys.Application;
import kubys.model.*;
import kubys.model.common.Breed;
import kubys.service.MapService;
import kubys.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

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

        mainMap = Map.builder().cells(new HashMap<>()).build();

        player = Player.builder()
                .breed(Breed.Dwarf)
                .currentMap(mainMap)
                .level(1)
                .name("Joalien")
                .pa(10)
                .pm(5)
                .build();


        position = Position.builder()
                .x(0)
                .y(1)
                .z(0)
                .build();

        nextPosition = Position.builder()
                .x(position.getX())
                .y(position.getY())
                .z(position.getZ()+1)
                .build();
        MapService.generateEmptyMap(Position.builder().build(), mainMap);
        MapService.addPlayer(mainMap, player, position);
    }

    @Test
    @DisplayName("move forward")
    void moveForward() {
        log.info(player.getPosition().toString());
        PlayerService.moveForward(player);
        assertEquals(player.getPosition(), nextPosition);
        log.info(player.getPosition().toString());
    }

    @Test
    @DisplayName("Try to move forward et fail")
    void tryToMoveForward() {
        log.info(player.getPosition().toString());
        mainMap.getCells().put(nextPosition, LandPlot.builder().build());
        assertSame(mainMap, player.getCurrentMap());
        PlayerService.moveForward(player);
        assertEquals(player.getPosition(), position);
        log.info(player.getPosition().toString());
    }
}
