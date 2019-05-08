package kubys.controller;

import kubys.model.Cell;
import kubys.model.Map;
import kubys.model.Player;
import kubys.model.common.Breed;
import kubys.service.Spells;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Slf4j
public class InitController {

    private Map map;
    private static int from = 1;


    @Autowired
    public InitController(Map map) {
        this.map = map;
    }

    @MessageMapping("/getAllMap")
    @SendToUser("/getAllMap")
    public Cell[] initMap(Principal principal, SimpMessageHeaderAccessor headerAccessor) {

        Player player = Player.builder()
                .id(from++)
                .breed(Breed.DWARF)
                .level(1)
                .name(principal.getName())
                .pa(10)
                .pm(5)
                .isConnected(true)
                .spells(Spells.getSpells())
                .build();

        this.map.getMapOfPlayer().put(headerAccessor.getSessionId(), player);
        log.debug("Simultaneous connected players  : "+this.map.getMapOfPlayer().size());

        log.debug("map : "+map.getCells());
        return map.getCells().values().toArray(new Cell[0]);
    }

    @MessageExceptionHandler
    @SendToUser("/errors")
    public String handleException(Throwable exception) {
        log.error(exception.toString());
        return exception.getMessage();
    }

}