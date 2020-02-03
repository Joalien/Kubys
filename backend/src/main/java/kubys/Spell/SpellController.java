package kubys.Spell;

import kubys.Player.Player;
import kubys.configuration.commons.SessionStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
@Slf4j
@AllArgsConstructor
public class SpellController {

    private SessionStore sessionStore;
    private SpellService spellService;

    //When a player make a command, only return the diff with previous map
    @MessageMapping("/getSpells")
    @SendToUser("/getSpells")
    public Spell[] directionPlayer(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        //Get the current player
        Player player = sessionStore.getPlayer();

        return spellService.getSpellsByBreed(player.getBreed()).toArray(new Spell[0]);
    }
}