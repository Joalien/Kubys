package kubys.controller;

import kubys.model.Cell;
import kubys.model.Map;
import kubys.model.Player;
import kubys.model.common.Direction;
import kubys.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
@Slf4j
public class MoveController {

    private Map map;

    @Autowired
    public MoveController(Map map) {
        this.map = map;
    }

    //When a player make a direction, only return the diff with previous map
    @MessageMapping("/move")
    @SendTo("/broker/move")
    public Cell directionPlayer(Principal principal, Direction direction,  SimpMessageHeaderAccessor headerAccessor) {
        log.info("Server side : "+ direction.toString());

        //Get the current player
        Player player = map.getMapOfPlayer().get(headerAccessor.getSessionId());

        log.error(SecurityContextHolder.getContext().toString());

        //If player move, send changes to all players
        if(PlayerService.movePlayer(player, direction)){
            return player;
        }
        return null;
    }

}