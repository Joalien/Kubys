package kubys.Fight;

import kubys.Player.Player;
import kubys.User.User;
import kubys.User.UserService;
import kubys.configuration.commons.ApplicationStore;
import kubys.configuration.commons.SessionStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

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

    @MessageExceptionHandler
    @SendToUser("/errors")
    public String handleException(Throwable exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }

}