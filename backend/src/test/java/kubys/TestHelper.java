package kubys;

import kubys.Spell.Breed;
import kubys.Player.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestHelper {

    public static Player createNewPlayer() {
        return Player.builder()
                .breed(Breed.values()[ThreadLocalRandom.current().nextInt(Breed.values().length)]) // random breed
                .level(ThreadLocalRandom.current().nextInt(1, 10))
                .name("Name" + ThreadLocalRandom.current().nextInt(1, 10))
                .pa(ThreadLocalRandom.current().nextInt(6, 12))
                .pm(ThreadLocalRandom.current().nextInt(4, 7))
                .build();
    }

    public static List<Player> createNewPlayers(int numberOfPlayers) {
        return IntStream.range(0, numberOfPlayers)
                .mapToObj(i -> createNewPlayer())
                .collect(Collectors.toList());
    }
}
