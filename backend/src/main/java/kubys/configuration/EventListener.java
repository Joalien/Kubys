package kubys.configuration;

import kubys.Fight.FightQueue;
import kubys.Player.Command;
import kubys.Player.Player;
import kubys.Player.PlayerDao;
import kubys.Player.PlayerService;
import kubys.configuration.commons.ApplicationStore;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@AllArgsConstructor
public class EventListener implements ApplicationListener<SessionDisconnectEvent> {

    private SimpMessagingTemplate template;
    private ApplicationStore applicationStore;
    private PlayerService playerService;
    private FightQueue fightQueue;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {

        String sessionId = sessionDisconnectEvent.getSessionId();
        log.info("USER DISCONNECT !");
        if (applicationStore.getSessionIdPlayer().containsKey(sessionId)) {
            Player oldPlayer = applicationStore.getSessionIdPlayer().get(sessionId);
            applicationStore.getSessionIdPlayer().remove(sessionId);
            fightQueue.removePlayer(oldPlayer);

            playerService.movePlayer(oldPlayer, Command.REMOVE);

            this.template.convertAndSend("/broker/command", oldPlayer);
        }



    }
}


