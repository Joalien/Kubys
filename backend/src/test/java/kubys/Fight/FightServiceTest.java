package kubys.Fight;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
class FightTest {

    @Autowired
    FightService fightService;

    @Test
    @DisplayName("create a new fight map")
    void createFightMaps() {
        fightService.generateFight();
    }


    @DisplayName("create two different fight map")
    void createFightMaps() {
        fightService.generateFight()
    }

    @Test
    @DisplayName("Verify that ApplicationStore is not null after initialisation")
    void testApplicationStoreNotNull() {
        assertNotNull(fightQueue.getApplicationStore());
    }

}