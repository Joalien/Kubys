//package kubys.Fight;
//
//import kubys.TestHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Collections;
//
//@SpringBootTest
//@Slf4j
//class FightServiceTest {
//
//    @Autowired
//    FightService fightService;
//
//    @Test
//    @DisplayName("create a new fight map")
//    void createFightMaps() {
//        fightService.generateFight(TestHelper.);
//    }
//
//
//    @DisplayName("create two different fight map")
//    void createFightMaps() {
//        fightService.generateFight()
//    }
//
//    @Test
//    @DisplayName("Verify that ApplicationStore is not null after initialisation")
//    void testApplicationStoreNotNull() {
//        assertNotNull(fightQueue.getApplicationStore());
//    }
//
//    @Test
//    @DisplayName("create a map with no player")
//    void createMapWithNoPlayer() {
//        Assertions.assertThrows(IllegalArgumentException.class, () -> fightService.generateFight(Collections.emptyList()));
//        Assertions.assertThrows(IllegalArgumentException.class, () -> fightService.generateFight(Collections.singletonList(TestHelper.)));
//    }
//
//
//@Test
//@DisplayName("If a player is already inside a fight, he can't join a new one")
//public void playerOnlyInOneFight() {}
//
//}