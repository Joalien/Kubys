package kubys.configuration.commons;

import kubys.Player.Player;
import kubys.User.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ApplicationScope
@Getter
@Setter
@ToString
@Builder
@Slf4j
public class ApplicationStore {

    @Builder.Default
    private Map<String, Player> sessionIdPlayer = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        // Invoked after dependencies injected
    }

    // ...

    @PreDestroy
    public void destroy() {
        // Invoked when the WebSocket session ends
    }

}

