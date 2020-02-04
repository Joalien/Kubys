package kubys.Fight;

import kubys.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
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
    @DisplayName("Improve me (find what depends on number of players inside a fight) ...")
    void testGenerateFight() {
        for (int i = 0; i < FightQueue.NUMBER_OF_PLAYER; i++) {
            fightQueue.addPlayer(TestHelper.createNewPlayer());
        }
        Assertions.assertThat(fightQueue.getWaitingQueue().size()).isEqualTo(FightQueue.NUMBER_OF_PLAYER);

        fightQueue.getLaunchFight().run();

        assertTrue(fightQueue.getWaitingQueue().isEmpty());
    }

    @Test
    @DisplayName("Verify that ApplicationStore is not null after initialisation")
    void testApplicationStoreNotNull() {
        assertNotNull(fightQueue.getApplicationStore());
    }

    @AfterEach
    void clearAllFight() {
        FightService.FIGHTS.clear();
    }


}