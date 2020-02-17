package kubys.fight;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;

@SpringBootTest
@Slf4j
class FightControllerTest {

    @Autowired
    FightController fightController;
    @Autowired
    FightController.FightUuidController fightUuidController;

    @Test
    @DisplayName("Verify waiting queue subscription")
    void subscribeWaitingQueue() {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId("sessionId");
        fightUuidController.endTurn(headerAccessor);
    }
}
