package kubys.fight;

import kubys.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class FightServiceTest {

    @Autowired
    FightService fightService;

    @Nested
    class generateFight {
        @BeforeEach
        void verifyThereIsNoFight() {
            assertTrue(fightService.getFights().isEmpty());
        }

        @Test
        void generateSimpleFight() {
            Fight fight = fightService.generateFight(TestHelper.createRandomNewPlayers(2));// Magic number!
            assertEquals(fightService.getFights().size(), 1);
            assertTrue(fightService.getFights().containsKey(fight.getUuid()));
            assertTrue(fightService.getFights().containsValue(fight));
        }

        @Test
        void createMultiplesFightMaps() {
            final int numberOfFight = 3;
            for (int i = 0; i < numberOfFight; i++) {
                fightService.generateFight(TestHelper.createRandomNewPlayers(2));// Magic number!
            }
            assertEquals(fightService.getFights().size(), numberOfFight);
        }

        @Test
        void createMapWithTooManyPlayers() {
            int tooManyPlayers = 10; // May depends on fight map starting position (should not introduce coupling)
            assertThrows(IllegalStateException.class, () -> fightService.generateFight(TestHelper.createRandomNewPlayers(tooManyPlayers)));
        }

        @Test
        void createMapWithTooFewPlayers() {
            int tooFewPlayers = fightService.MIN_NUMBER_OF_PLAYER - 1;
            assertThrows(IllegalArgumentException.class, () -> fightService.generateFight(TestHelper.createRandomNewPlayers(tooFewPlayers)));
        }

        @AfterEach
        void clearAllFight() {
            fightService.getFights().clear();
        }
    }

    @Nested
    class winFight {
        Fight fight;

        @BeforeEach
        void winTheOnlyFight() {
            this.fight = fightService.generateFight(TestHelper.createRandomNewPlayers(2));
        }

        @Test
        void fightIsRemoved() {
            assertTrue(fightService.getFights().containsKey(fight.getUuid()));

            fightService.winFight(fight.getUuid());

            assertFalse(fightService.getFights().containsKey(fight.getUuid()));
        }

        @Test
        void allPlayersAreMovedFromFight() {
            assertTrue(fight.getPlayers().stream().allMatch(player -> player.getMap().equals(fight.getMap())));

            fightService.winFight(fight.getUuid());

            assertFalse(fight.getPlayers().stream().anyMatch(player -> player.getMap().equals(fight.getMap())));
        }
    }
}
