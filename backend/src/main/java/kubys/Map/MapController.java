package kubys.Map;

import kubys.Map.Model.Cell;
import kubys.Player.Command;
import kubys.Player.Player;
import kubys.Player.PlayerService;
import kubys.configuration.commons.SessionStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Slf4j
@AllArgsConstructor
public class MapController {

    private SessionStore sessionStore;

    @MessageMapping("/getAllMap")
    @SendToUser("/getAllMap")
    public Cell[] getAllMap(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        Player player = sessionStore.getPlayer();
        if (player.getPosition() == null || player.getMap() == null) {
            PlayerService.movePlayer(player, Command.CREATE);
        }

        return player.getMap().getCells().values().toArray(new Cell[0]);
    }

    @MessageExceptionHandler
    @SendToUser("/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}