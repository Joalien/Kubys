package kubys.controller;

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
import java.util.LinkedHashSet;
import java.util.Set;

@Controller
@Slf4j
public class PlayerController {

    private Map map;
    private static Long from = 1L;


    @Autowired
    public PlayerController(Map map) {
        this.map = map;
    }

    @MessageMapping("/getPlayers")
    @SendToUser("/getPlayers")
    public Player[] initMap(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        //TODO : Get players from db
        Set<Player> players = new LinkedHashSet<>();
        Player.PlayerBuilder playerBuilder = Player.builder()
                .spells(Spells.getSpells())
                .id(from++)
                .level(1)
                .name(principal.getName())
                .pa(10)
                .pm(5);

        for(Breed breed : Breed.values()){
            players.add(playerBuilder.breed(breed).build());
        }

//        log.debug(players.toString());

        return players.toArray(new Player[0]);

    }

    @MessageExceptionHandler
    @SendToUser("/errors")
    public String handleException(Throwable exception) {
        log.error(exception.toString());
        return exception.getMessage();
    }

}