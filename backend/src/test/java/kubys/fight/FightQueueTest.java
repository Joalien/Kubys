package kubys.fight;

import kubys.Player.Player;
import kubys.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    FightService fightService;
    @Autowired
    FightController.FightUuidController fightUuidController;

    @BeforeEach
    void verifyThatQueueAndFightsAreEmpty() {
        assertTrue(fightService.getFights().isEmpty());
        assertTrue(fightQueue.getWaitingQueue().isEmpty());
    }

    @Test
    void generateFightFromWaitingQueue() {
        for (int i = 0; i < fightQueue.NUMBER_OF_PLAYER; i++) {
            assertTrue(fightQueue.addPlayer(TestHelper.createRandomNewPlayer()));
        }
        Assertions.assertThat(fightQueue.getWaitingQueue().size()).isEqualTo(fightQueue.NUMBER_OF_PLAYER);

        fightQueue.getLaunchFight().run();

        Assertions.assertThat(fightService.getFights().size()).isEqualTo(1);
        assertTrue(fightQueue.getWaitingQueue().isEmpty());
    }

    @Test
    void removePlayerFromWaitingQueue() {
        Player player = TestHelper.createRandomNewPlayer();
        assertTrue(fightQueue.addPlayer(player));
        Assertions.assertThat(fightQueue.getWaitingQueue().size()).isEqualTo(1);

        assertTrue(fightQueue.removePlayer(player));

        assertTrue(fightQueue.getWaitingQueue().isEmpty());
        assertTrue(fightService.getFights().isEmpty());
    }

    @Test
    void nothingHappenIfFightQueueEmpty() {
        assertTrue(fightQueue.getWaitingQueue().isEmpty());
        assertTrue(fightService.getFights().isEmpty());
        fightQueue.getLaunchFight().run();
        assertTrue(fightQueue.getWaitingQueue().isEmpty());
        assertTrue(fightService.getFights().isEmpty());
    }

    @Test
    void testApplicationStoreNotNull() {
        assertNotNull(fightQueue.getApplicationStore());
    }

    @AfterEach
    void clearAllFight() {
        fightService.getFights().clear();
        fightQueue.getWaitingQueue().clear();
    }
}