package kubys.Map;

import kubys.Player.Player;
import kubys.Player.PlayerService;
import kubys.configuration.commons.SessionStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
@Slf4j
@AllArgsConstructor
public class MoveController {

    private SessionStore sessionStore;

    //When a player make a command, only return the diff with previous map
    @MessageMapping("/command")
    @SendTo("/broker/command")
    public Cell directionPlayer(Principal principal, Command command, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Command controller");

        //Get the current player
        Player player = sessionStore.getPlayer();

        //If player command, send changes to all players
        if(PlayerService.movePlayer(player, command)){
            return player;
        }
        return null;
    }

}