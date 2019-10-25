package kubys.Spell;

import kubys.Map.Map;
import kubys.Player.Player;
import kubys.configuration.commons.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
@Slf4j
public class SpellController {

    private SessionStore sessionStore;
    private SpellService spellService;

    @Autowired
    public SpellController(Map map, SessionStore sessionStore, SpellService spellService) {
        this.sessionStore = sessionStore;
        this.spellService = spellService;
    }

    //When a player make a command, only return the diff with previous map
    @MessageMapping("/getSpells")
    @SendToUser("/getSpells")
    public Spell [] directionPlayer(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        //Get the current player
        Player player = sessionStore.getPlayer();

        log.info(spellService.getSpellsByPlayer(player).toString());

        //If player command, send changes to all players
        return spellService.getSpellsByPlayer(player).toArray(new Spell[0]);
    }
}