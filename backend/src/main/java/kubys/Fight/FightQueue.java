package kubys.Fight;

import kubys.Player.Player;
import kubys.configuration.commons.ApplicationStore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
public class FightQueue {

    private Set<Player> waitingQueue = ConcurrentHashMap.newKeySet();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final int NUMBER_OF_PLAYER = 2;
    public static final int DELAY = 10; // Delay in seconds each time the queue is processed
    @NonNull
    private SimpMessagingTemplate template;
    @NonNull
    private ApplicationStore applicationStore;

    private Runnable launchFight = () -> {
        while (waitingQueue.size() >= NUMBER_OF_PLAYER) {
            log.info("Waiting queue : " + waitingQueue);
            List<Player> players = waitingQueue
                    .stream()
                    .limit(NUMBER_OF_PLAYER)
                    .collect(Collectors.toList());
            waitingQueue.removeAll(players);
            Fight fight = generateFight(players);
            //TODO make a real fight
            log.info("Players : " + players);
            players.forEach(
                    player -> this.template.convertAndSendToUser(
                            applicationStore.getStringFromPlayer(player),
                            "/fight",
                            fight.getId()
                    )
            );
        }
    };

    @PostConstruct
    public void addScheduler(){
        scheduler.scheduleWithFixedDelay(launchFight, 0, DELAY, TimeUnit.SECONDS);
        log.info(String.valueOf(applicationStore));
    }

    public boolean addPlayer(Player player){
        return waitingQueue.add(player);
    }

    private Fight generateFight(List<Player> players) {
        return new Fight.FightBuilder()
                .players(players)
                .build();
    }


    public boolean removePlayer(Player player) {
        return waitingQueue.remove(player);
    }
}
