package kubys.configuration.commons;

import kubys.Player.Player;
import kubys.User.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.NoSuchElementException;
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

    public String getStringFromPlayer(Player p) {
        return sessionIdPlayer.keySet()
                .stream()
                .filter(s -> sessionIdPlayer.get(s).equals(p))
                .peek(s -> log.info("Reverse dictionary : " + s))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(("No string are key for this player")));
    }

}

