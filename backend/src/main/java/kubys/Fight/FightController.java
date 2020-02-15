package kubys.Fight;

import kubys.configuration.commons.ApplicationStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
    private FightService fightService;

    @MessageMapping("/subscribe")
    public void subscribe(SimpMessageHeaderAccessor headerAccessor) {
        log.info("/subscribe");
        fightQueue.addPlayer(applicationStore.getSessionIdPlayer().get(headerAccessor.getSessionId()));
    }

    @MessageMapping("/unsubscribe")
    public void unsubscribe(SimpMessageHeaderAccessor headerAccessor) {
        log.info("/unsubscribe");
        fightQueue.removePlayer(applicationStore.getSessionIdPlayer().get(headerAccessor.getSessionId()));
    }

    @Controller
    @MessageMapping("/fight/{fightUuid}")
    @SendTo("/broker/fight/{fightUuid}")
    class fightUuidController {

        //TODO Add @Param to properly handle method argument
        @MessageMapping("/move/{cell}")
        @SendTo("/broker/fight/{fightUuid}")
        public void moveToCell(SimpMessageHeaderAccessor headerAccessor, int cell) {
            System.out.println("moveToCell " + cell);
        }

        @MessageMapping("/use/{spell}/on/{cell}")
        @SendTo("/broker/fight/{fightUuid}")
        public void useSpellOnCell(SimpMessageHeaderAccessor headerAccessor, long spell, int cell) {
            System.out.println("use " + spell + " on " + " cell");
        }

        @MessageMapping("/endTurn")
        @SendTo("/broker/fight")
        public void endTurn(SimpMessageHeaderAccessor headerAccessor) {
            System.out.println("endTurn");
        }

        @MessageMapping("/win")
        @SendTo("/broker/fight/{fightUuid}")
        public boolean winFight(SimpMessageHeaderAccessor headerAccessor, @DestinationVariable String fightUuid) {
            System.out.println("winFight : " + fightUuid + " uuid");
            fightService.winFight(fightUuid);
            return true;
        }
    }

    @MessageExceptionHandler
    @SendToUser("/errors")
    public String handleException(Throwable exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }
}