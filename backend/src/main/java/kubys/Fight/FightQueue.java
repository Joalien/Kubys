package kubys.Fight;

import kubys.Player.Player;
import kubys.configuration.commons.ApplicationStore;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor // Instantiate only non null variables
public class FightQueue {

    private Set<Player> waitingQueue = ConcurrentHashMap.newKeySet();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public final int NUMBER_OF_PLAYER = 1;
    private final int DELAY = 10; // Delay in seconds each time the queue is processed

    @NonNull
    private SimpMessagingTemplate template;
    @NonNull
    private ApplicationStore applicationStore;
    @NonNull
    private FightService fightService;

    private Runnable launchFight = () -> {
//        log.info("waitingQueue : " + waitingQueue);
        List<Player> players = waitingQueue
                .stream()
                .limit(NUMBER_OF_PLAYER)
                .collect(Collectors.toList());
        if (waitingQueue.removeAll(players)) {
            Fight fight = fightService.generateFight(players);
            log.info("Fight : " + fight);
            try {
                players.forEach(player -> this.template.convertAndSend("/broker/fight", fight.getUuid()));
            } catch (NoSuchElementException | NullPointerException e) { // Happens in tests when no user in applicationStore
                log.error(e.toString());
            }
        }
    };

    @PostConstruct
    void addScheduler() {
        scheduler.scheduleWithFixedDelay(launchFight, 0, DELAY, TimeUnit.SECONDS);
        log.info(String.valueOf(applicationStore));
    }

    boolean addPlayer(Player player) {
        return waitingQueue.add(player);
    }

    public boolean removePlayer(Player player) {
        return waitingQueue.remove(player);
    }

    public void clearQueue() {
        this.getWaitingQueue().clear();
    }
}
