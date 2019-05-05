package kubys.controller;

import kubys.model.Cell;
import kubys.model.Map;
import kubys.model.Player;
import kubys.model.common.Command;
import kubys.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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

    //When a player make a command, only return the diff with previous map
    @MessageMapping("/command")
    @SendTo("/broker/command")
    public Cell directionPlayer(Principal principal, Command command, SimpMessageHeaderAccessor headerAccessor) {
        log.info("Server side : "+ command.toString());

        //Get the current player
        Player player = map.getMapOfPlayer().get(headerAccessor.getSessionId());

        //If player command, send changes to all players
        if(PlayerService.movePlayer(player, command)){
            return player;
        }
        return null;
    }

}