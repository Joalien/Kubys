package kubys.Player;

import kubys.Map.Map;
import kubys.Spell.Spells;
import kubys.User.User;
import kubys.User.UserDao;
import kubys.User.UserService;
import kubys.configuration.commons.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j

public class PlayerController {

    private UserService userService;
    private SessionStore sessionStore;


    @Autowired
    public PlayerController(UserService userService, SessionStore sessionStore) {
        this.userService = userService;
        this.sessionStore = sessionStore;
    }
    @MessageMapping("/setPlayer")
    @SendToUser("/setPlayer")
    public Player setPlayer(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        log.info("setPlayer controller");
        
        User u = userService.findById(principal.getName());
        Player player = u.getPlayers().get(0);

        log.info(u.toString());
        log.info(player.toString());

        sessionStore.setPlayer(player);
        sessionStore.setUser(u);

        return player;
    }




    @MessageMapping("/getPlayers")
    @SendToUser("/getPlayers")
    public Player[] initMap(Principal principal, SimpMessageHeaderAccessor headerAccessor) {

        User u = userService.findById(principal.getName());

        System.out.println(u);
        if(u.getPlayers().isEmpty()) {
            u.setPlayers(List.of(
                    Player.builder()
                            .breed(Breed.DWARF)
                            .user(u)
                            .level(1)
                            .name("Harisson")
                            .pa(10)
                            .pm(5)
                            .spells(Spells.getSpells())
                            .build()
            ));
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