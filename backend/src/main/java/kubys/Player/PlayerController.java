package kubys.Player;

import kubys.User.User;
import kubys.User.UserService;
import kubys.configuration.commons.ApplicationStore;
import kubys.configuration.commons.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Slf4j

public class PlayerController {

    private UserService userService;
    private SessionStore sessionStore;
    private ApplicationStore applicationStore;

    @Autowired
    public PlayerController(UserService userService, SessionStore sessionStore, ApplicationStore applicationStore) {
        this.userService = userService;
        this.sessionStore = sessionStore;
        this.applicationStore = applicationStore;
    }

    @MessageMapping("/setPlayer")
    @SendToUser("/setPlayer")
    public Player setPlayer(@Payload Integer playerIndex, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
//        log.info("setPlayer controller");
//        log.info(String.valueOf(playerIndex));

        User u = userService.findById(principal.getName());
        Player player = u.getPlayers().get(playerIndex);

//        log.info(u.toString());
//        log.info(player.toString());
//        log.info(principal.toString());
//        log.info(headerAccessor.toString());
        applicationStore.getSessionIdPlayer().put(headerAccessor.getSessionId(), player);

        sessionStore.setPlayer(player);
        sessionStore.setUser(u);

        return player;
    }




    @MessageMapping("/getPlayers")
    @SendToUser("/getPlayers")
    public Player[] initMap(Principal principal, SimpMessageHeaderAccessor headerAccessor) {

        User u = userService.findById(principal.getName());

        log.info(String.valueOf(u));
        if(u.getPlayers().isEmpty()) {
            log.error("Should never occur except first time if no automatic player in webSocketConfig!");
        }

        return u.getPlayers().toArray(new Player[0]);
    }

    @MessageExceptionHandler
    @SendToUser("/errors")
    public String handleException(Throwable exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }

}