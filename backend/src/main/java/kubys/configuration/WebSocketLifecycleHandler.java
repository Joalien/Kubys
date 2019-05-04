package kubys.configuration;

import kubys.model.Map;
import kubys.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class WebSocketLifecycleHandler extends TextWebSocketHandler implements WebSocketHandler {

    private Map map;


    @Autowired
    public WebSocketLifecycleHandler(Map map) {
        this.map = map;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
//        if(this.map.getMapOfPlayer().containsKey(webSocketSession.getPrincipal())) {
//            log.error("USER DISCONNECT, DELETE HIS PLAYER !");
//            Player oldPlayer = this.map.getMapOfPlayer().get(webSocketSession.getPrincipal());
//            this.map.getCells().remove(oldPlayer.getPosition(), oldPlayer);
//            this.map.getMapOfPlayer().remove(webSocketSession.getPrincipal(), oldPlayer);
//        } else log.error("toDelete");
    }

}
