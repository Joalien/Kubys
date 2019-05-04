package kubys.configuration;

import kubys.model.Map;
import kubys.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Slf4j
@Component
public class EventListener implements ApplicationListener<SessionDisconnectEvent> {

    private Map map;


    @Autowired
    public EventListener(Map map) {
        this.map = map;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {

        log.error(sessionDisconnectEvent.getSessionId());
        log.error(this.map.getMapOfPlayer().toString());

        if(this.map.getMapOfPlayer().containsKey(sessionDisconnectEvent.getSessionId())) {
            log.error("USER DISCONNECT, DELETE HIS PLAYER !");
            Player oldPlayer = this.map.getMapOfPlayer().get(sessionDisconnectEvent.getSessionId());
            this.map.getCells().remove(oldPlayer.getPosition(), oldPlayer);
            this.map.getMapOfPlayer().remove(sessionDisconnectEvent.getSessionId(), oldPlayer);
        } else log.error("toDelete");
    }
}
