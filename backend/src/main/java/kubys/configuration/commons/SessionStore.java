package kubys.configuration.commons;

import kubys.player.Player;
import kubys.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
@ToString
public class SessionStore {

    private User user;
    private Player player;
}

