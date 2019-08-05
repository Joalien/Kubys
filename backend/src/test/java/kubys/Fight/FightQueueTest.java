package kubys.Fight;

import kubys.Player.Player;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class FightQueueTest {

    @Autowired
    FightQueue fightQueue;

    @Test
    @DisplayName("Improve me ...")
    void testGenerateFight() throws InterruptedException {
        fightQueue.addPlayer(Player.builder().id(1L).name("Player 1").build());
        fightQueue.addPlayer(Player.builder().id(2L).name("Player 2").build());
        log.debug(fightQueue.getWaitingQueue().stream().map(Player::getName).collect(Collectors.joining()));
        assertFalse(fightQueue.getWaitingQueue().isEmpty());

        fightQueue.getLaunchFight().run();

        log.debug(fightQueue.getWaitingQueue().stream().map(Player::getName).collect(Collectors.joining()));
        assertTrue(fightQueue.getWaitingQueue().isEmpty());
    }

    @Test
    @DisplayName("Verify that ApplicationStore is not null after initialisation")
    void testApplicationStoreNotNull() {
        assertNotNull(fightQueue.getApplicationStore());
    }

}