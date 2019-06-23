package kubys.configuration;

import kubys.Map.Map;
import kubys.Player.Player;
import kubys.Player.PlayerService;
import kubys.configuration.commons.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class EventListener implements ApplicationListener<SessionDisconnectEvent> {

    private Map map;
    private SimpMessagingTemplate template;
    private SessionStore sessionStore;
    private PlayerService playerService;

    @Autowired
    public EventListener(Map map, SimpMessagingTemplate template, SessionStore sessionStore, PlayerService playerService) {
        this.map = map;
        this.template = template;
        this.sessionStore = sessionStore;
        this.playerService = playerService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {

        log.info("USER DISCONNECT !");
        Player oldPlayer = sessionStore.getPlayer();
        this.map.getCells().remove(oldPlayer.getPosition(), oldPlayer);
        playerService.save(oldPlayer);

        //TODO Create new endpoint for disconnected users
        this.template.convertAndSend("/broker/command", oldPlayer);


    }
}
