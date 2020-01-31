package kubys.Fight;

import kubys.Player.Player;
import kubys.configuration.commons.ApplicationStore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
public class FightQueue {

    private Set<Player> waitingQueue = ConcurrentHashMap.newKeySet();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final int NUMBER_OF_PLAYER = 1;
    public static final int DELAY = 10; // Delay in seconds each time the queue is processed
    @NonNull
    private SimpMessagingTemplate template;
    @NonNull
    private ApplicationStore applicationStore;

    private Runnable launchFight = () -> {
        while (waitingQueue.size() >= NUMBER_OF_PLAYER) {
            List<Player> players = waitingQueue
                    .stream()
                    .limit(NUMBER_OF_PLAYER)
                    .collect(Collectors.toList());
            waitingQueue.removeAll(players);
            Fight fight = generateFight(players);
            //TODO make a real fight
            log.info("Fight : " + fight);
            try {
                players.forEach(
                        player -> {
                            String sessionId = applicationStore.getStringFromPlayer(player);
                            this.template.convertAndSendToUser(
                                    sessionId,
                                    "/fight",
                                    fight.getUuid(),
                                    createHeaders(sessionId)
                            );
                        }
                );
            } catch (NoSuchElementException | NullPointerException e) { // Happens in tests when no user in applicationStore
                log.error(e.toString());
            }
        }
    };

    @PostConstruct
    public void addScheduler(){
        scheduler.scheduleWithFixedDelay(launchFight, 0, DELAY, TimeUnit.SECONDS);
        log.info(String.valueOf(applicationStore));
    }

    public boolean addPlayer(Player player) {
        return waitingQueue.add(player);
    }

    private Fight generateFight(List<Player> players) {
        return new Fight.FightBuilder()
                .uuid(UUID.randomUUID().toString())
                .players(players)
                .build();
    }

    public boolean removePlayer(Player player) {
        return waitingQueue.remove(player);
    }

    private static MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
