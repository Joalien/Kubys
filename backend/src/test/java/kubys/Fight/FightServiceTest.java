package kubys.Fight;

import kubys.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class FightServiceTest {

    @Autowired
    FightService fightService;

    @BeforeEach
    void verifyThereIsNoFight() {
        assertTrue(FightService.FIGHTS.isEmpty()); // May be wrong if an other test has created a fight
    }

    @Test
    @DisplayName("create a simple fight map")
    void createFightMap() {
        Fight fight = fightService.generateFight(TestHelper.createNewPlayers(2));// Magic number!
        assertEquals(FightService.FIGHTS.size(), 1);
        assertTrue(FightService.FIGHTS.containsKey(fight.getUuid()));
        assertTrue(FightService.FIGHTS.containsValue(fight));
    }

    @Test
    @DisplayName("create three different fight map")
    void createMultiplesFightMaps() {
        fightService.generateFight(TestHelper.createNewPlayers(2));// Magic number!
        fightService.generateFight(TestHelper.createNewPlayers(2));// Magic number!
        fightService.generateFight(TestHelper.createNewPlayers(2));// Magic number!
        assertEquals(FightService.FIGHTS.size(), 3);
    }

    @Test
    @DisplayName("verify map can't afford more player that has staring position")
    void createMapWithTooManyPlayers() {
        int tooManyPlayers = 10; // May depends on fight map starting position (should not introduce coupling)
        assertThrows(IllegalStateException.class, () -> fightService.generateFight(TestHelper.createNewPlayers(tooManyPlayers)));
    }

    @Test
    @DisplayName("create map with no player")
    void createMapWithTooFewPlayers() {
        int tooFewPlayers = FightService.MIN_NUMBER_OF_PLAYER - 1;
        assertThrows(IllegalArgumentException.class, () -> fightService.generateFight(TestHelper.createNewPlayers(tooFewPlayers)));
    }

    @AfterEach
    void clearAllFight() {
        FightService.FIGHTS.clear();
    }
}
