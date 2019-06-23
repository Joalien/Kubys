package kubys.Spell;

import kubys.Map.Map;
import kubys.Player.Player;
import kubys.configuration.commons.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
@Slf4j
public class SpellController {

    private Map map;
    private SessionStore sessionStore;

    @Autowired
    public SpellController(Map map, SessionStore sessionStore) {
        this.map = map;
        this.sessionStore = sessionStore;
    }

    //When a player make a command, only return the diff with previous map
    @MessageMapping("/getSpells")
    @SendToUser("/getSpells")
    public Spell [] directionPlayer(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
//        log.info("Server side : Spell");

        //Get the current player
        Player player = sessionStore.getPlayer();

        //If player command, send changes to all players
        return player.getSpells().toArray(new Spell[0]);

    }

}