package kubys.Fight;

import kubys.Player.Player;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class FightQueueTest {

    @Autowired
    FightQueue fightQueue;

    @Test
    @DisplayName("Improve me ...")
    void testGenerateFight() {
        fightQueue.addPlayer(Player.builder().id(1L).name("Player 1").build());
        fightQueue.addPlayer(Player.builder().id(2L).name("Player 2").build());
        Assertions.assertThat(fightQueue.getWaitingQueue().size()).isEqualTo(2);

        fightQueue.getLaunchFight().run();

        assertTrue(fightQueue.getWaitingQueue().isEmpty());
    }

    @Test
    @DisplayName("Verify that ApplicationStore is not null after initialisation")
    void testApplicationStoreNotNull() {
        assertNotNull(fightQueue.getApplicationStore());
    }

}