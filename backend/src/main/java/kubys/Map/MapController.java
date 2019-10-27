package kubys.Map;

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

    private Map map;
    private SessionStore sessionStore;

    @MessageMapping("/getAllMap")
    @SendToUser("/getAllMap")
    public Cell[] initMap(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        log.info("SessionStore in getAllMap controller : " + sessionStore);
        Player player = sessionStore.getPlayer();
        log.info(player.toString());
        if(player.getPosition() == null) PlayerService.movePlayer(player, Command.CREATE);
        log.info(player.toString());
        map.getCells().put(player.getPosition(), player);

        log.info("print all players on the map :");
        map.getCells().keySet().parallelStream().
                filter(position -> map.getCells().get(position) instanceof Player)
                .forEach(x -> System.out.println(x + " : " + map.getCells().get(x)));
        return map.getCells().values().toArray(new Cell[0]);
    }

    @MessageExceptionHandler
    @SendToUser("/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}