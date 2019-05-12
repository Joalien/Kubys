package kubys.configuration;

import kubys.model.Map;
import kubys.model.Player;
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

    @Autowired
    public EventListener(Map map, SimpMessagingTemplate template) {
        this.map = map;
        this.template = template;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {

        log.debug(sessionDisconnectEvent.getSessionId());
        log.debug(this.map.getMapOfPlayer().toString());

        if(this.map.getMapOfPlayer().containsKey(sessionDisconnectEvent.getSessionId())) {
            log.debug("USER DISCONNECT, DELETE HIS PLAYER !");
            Player oldPlayer = this.map.getMapOfPlayer().get(sessionDisconnectEvent.getSessionId());
            this.map.getCells().remove(oldPlayer.getPosition(), oldPlayer);
            this.map.getMapOfPlayer().remove(sessionDisconnectEvent.getSessionId(), oldPlayer);

            oldPlayer.setConnected(false);
            this.template.convertAndSend("/broker/command", oldPlayer);

        } else log.warn("Weird, socket disconnect without having associate player. Try to remove this warning !");
    }
}
