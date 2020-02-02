package kubys.Fight;

import kubys.configuration.commons.ApplicationStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@MessageMapping("/fight")
@AllArgsConstructor
public class FightController {

    private FightQueue fightQueue;
    private ApplicationStore applicationStore;

    @MessageMapping("/subscribe")
    public void subscribe(SimpMessageHeaderAccessor headerAccessor) {
        fightQueue.addPlayer(applicationStore.getSessionIdPlayer().get(headerAccessor.getSessionId()));
    }

    @MessageMapping("/unsubscribe")
    public void unsubscribe(SimpMessageHeaderAccessor headerAccessor) {
        fightQueue.removePlayer(applicationStore.getSessionIdPlayer().get(headerAccessor.getSessionId()));
    }

    @Controller
    @MessageMapping("/fight")
    @SendTo("/broker/fight/{fightId}")
    public static class FightIdController {

        @MessageMapping("/{fightId}/move/{cell}")
        @SendTo("/broker/fight/{fightId}")
        public void moveToCell(SimpMessageHeaderAccessor headerAccessor, int cell) {
            System.out.println("moveToCell " + cell);
        }

        @MessageMapping("/{fightId}/use/{spell}/on/{cell}")
        @SendTo("/broker/fight/{fightId}")
        public void useSpellOnCell(SimpMessageHeaderAccessor headerAccessor, long spell, int cell) {
            System.out.println("use " + spell + " on " + " cell");
        }

        @MessageMapping("/{fightId}/endTurn")
        @SendTo("/broker/fight/{fightId}")
        public void endTurn(SimpMessageHeaderAccessor headerAccessor) {
            System.out.println("endTurn");
        }

        @MessageMapping("/{fightId}/winGame")
        @SendTo("/broker/fight/{fightId}")
        public void toRename(SimpMessageHeaderAccessor headerAccessor) {
            System.out.println("winGame");
        }

    }

    @MessageExceptionHandler
    @SendToUser("/errors")
    public String handleException(Throwable exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }
}