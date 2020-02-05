package kubys.Player;

import kubys.Map.Cell;
import kubys.Spell.Spell;
import kubys.Spell.SpellService;
import kubys.User.User;
import kubys.User.UserService;
import kubys.configuration.commons.ApplicationStore;
import kubys.configuration.commons.SessionStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Slf4j
@AllArgsConstructor
public class PlayerController {

    private UserService userService;
    private SessionStore sessionStore;
    private ApplicationStore applicationStore;
    private SpellService spellService;
    private PlayerService playerService;

    @MessageMapping("/setPlayer")
    @SendToUser("/setPlayer")
    public long setPlayer(@Payload Integer playerIndex, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        User u = userService.findById(principal.getName());
        Player player = u.getPlayers().get(playerIndex);

        applicationStore.getSessionIdPlayer().put(headerAccessor.getSessionId(), player);

        sessionStore.setPlayer(player);
        sessionStore.setUser(u);

        return player.getId();
    }

    @MessageMapping("/getPlayers")
    @SendToUser("/getPlayers")
    public Player[] initMap(Principal principal, SimpMessageHeaderAccessor headerAccessor) {

        User u = userService.findById(principal.getName());

        log.info(String.valueOf(u));
        if(u.getPlayers().isEmpty()) {
            log.error("Should never occur except first time if no automatic player in webSocketConfig!");
        }

        return u.getPlayers().toArray(new Player[0]);
    }

    @MessageMapping("/getAllMap")
    @SendToUser("/getAllMap")
    public Cell[] getAllMap(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        log.info("getAllMap");
        Player player = sessionStore.getPlayer();
        if (player.getPosition() == null || player.getMap() == null) {
            PlayerService.movePlayer(player, Command.CREATE);
        }

        return player.getMap().getCells().values().toArray(new Cell[0]);
    }

    @MessageMapping("/getSpells")
    @SendToUser("/getSpells")
    public Spell[] getSpells(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        //Get the current player
        Player player = sessionStore.getPlayer();

        return spellService.getSpellsByBreed(player.getBreed()).toArray(new Spell[0]);
    }

    @MessageMapping("/getSpellPoints")
    @SendToUser("/getSpellPoints")
    public int getSpellPoints(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        //Get the current player
        Player player = sessionStore.getPlayer();

        return playerService.getSpellPoints(player);
    }

    //When a player make a command, only return the diff with previous map
    @MessageMapping("/command")
    @SendTo("/broker/command")
    public Cell applyCommand(Principal principal, Command command, SimpMessageHeaderAccessor headerAccessor) {
        Player player = sessionStore.getPlayer();

        //If player command, send changes to all players
        if (PlayerService.movePlayer(player, command)) {
            return player;
        }
        return null;
    }

    @MessageExceptionHandler
    @SendToUser("/errors")
    public String handleException(Throwable exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }

}