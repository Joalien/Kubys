package kubys;

import kubys.player.Player;
import kubys.spell.Dwarf;
import kubys.user.User;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestHelper {

    public static Player createRandomNewPlayer(User user) {
        return Player.builder()
                .user(user)
                .level(ThreadLocalRandom.current().nextInt(1, 10))
                .name("Name" + ThreadLocalRandom.current().nextInt(1, 10))
                .pa(ThreadLocalRandom.current().nextInt(6, 12))
                .pm(ThreadLocalRandom.current().nextInt(4, 7))
                .characteristics(Set.of(new Dwarf()))
                .build();
    }

    public static Player createRandomNewPlayer() {
        return createRandomNewPlayer(null);
    }

    public static List<Player> createRandomNewPlayers(int numberOfPlayers, User user) {
        return IntStream.range(0, numberOfPlayers)
                .mapToObj(i -> createRandomNewPlayer(user))
                .collect(Collectors.toList());
    }

    public static List<Player> createRandomNewPlayers(int numberOfPlayers) {
        return createRandomNewPlayers(numberOfPlayers, null);
    }
}
