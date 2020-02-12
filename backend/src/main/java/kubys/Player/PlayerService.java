package kubys.Player;

import kubys.Map.Cell;
import kubys.Map.Map;
import kubys.Map.MapService;
import kubys.Map.Position;
import kubys.Spell.Spell;
import kubys.Spell.SpellWrapper;
import kubys.User.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
@AllArgsConstructor
public class PlayerService {
    static private MapService mapService;
    static private SimpMessagingTemplate template;

    private PlayerDao playerDao;

    public static Boolean movePlayer(Player player, Command command) {
        switch(command) {
            case FORWARD:
                return PlayerService.moveForward(player);
            case BACKWARD:
                return PlayerService.moveBackward(player);
            case LEFT:
                return PlayerService.moveLeft(player);
            case RIGHT:
                return PlayerService.moveRight(player);
            case CREATE:
                PlayerService.switchMap(player, MapService.MAIN_MAP);
                // Make user fall if he is created in the air
                return moveToPosition(player, player.getPosition());
            default:
                throw new IllegalArgumentException("Unexpected value: " + command);
        }
    }

    private static Boolean moveForward(Player player) {
        Position position = player.getPosition().plusZ(1);
        return moveToPosition(player, position);
    }

    private static Boolean moveBackward(Player player) {
        Position position = player.getPosition().plusZ(-1);

        return moveToPosition(player, position);
    }

    private static Boolean moveLeft(Player player) {
        Position position = player.getPosition().plusX(-1);

        return moveToPosition(player, position);
    }

    private static Boolean moveRight(Player player) {
        Position position = player.getPosition().plusX(1);

        return moveToPosition(player, position);
    }

    // This method contains logic
    private static Boolean moveToPosition(Player player, Position position) {
        java.util.Map<Position, Cell> cells = player.getMap().getCells();
        if (position == null || !player.getMap().isInsideMap(position)) throw new IllegalArgumentException("Position should not be null nor outside of the map !");
        Position above = position.plusY(1);
        Position below = position.plusY(-1);

        // Make user fall if he is created in the air
        if(!cells.containsKey(position) || cells.get(position).equals(player)) {
            // Drop the ~mic~ player
            while (!cells.containsKey(below)) {
                below = below.plusY(-1);
                if (!player.getMap().isInsideMap(below)) {
                    return false;
                }
            }
            updatePosition(player, below.plusY(1));
            return true;
        } else if (!cells.containsKey(above) && !cells.containsKey(player.getPosition().plusY(1))) {
            updatePosition(player, above);
            return true;
        } else return false;
    }

    private static void updatePosition(Player player, Position position) {
        java.util.Map<Position, Cell> cells = player.getMap().getCells();
        cells.remove(player.getPosition());
        cells.put(position, player);

        // If they were a player on the cell above
        // Refactor if other cells's children need to drop (create intermediary class, implement behaviour ...)
        if(cells.containsKey(player.getPosition().plusY(1)) &&
                cells.get(player.getPosition().plusY(1)) instanceof Player) {
            moveToPosition((Player) cells.get(player.getPosition().plusY(1)), player.getPosition());
            PlayerService.template.convertAndSend("/broker/command", cells.get(player.getPosition()));
        }

        player.setPosition(position);
    }

    public static boolean switchMap(Player player, Map destinationMap) {
        try {
            player.getMap().removeCell(player.getPosition());
        } catch (NullPointerException ignored) {
            // Might happens if user has no map nor position
        }
        Optional<Position> newPosition = destinationMap.getStartingPositions()
                .stream()
                .filter(p -> !destinationMap.getCells().containsKey(p))
                .findAny();

        if (newPosition.isEmpty()) {
            return false;
        }

        //Should be atomic
        destinationMap.addCell(newPosition.get(), player);
        player.setMap(destinationMap);
        player.setPosition(newPosition.get());
        return true;
    }

    public int getSpellPoints(Player player) {
        int POINTS_MODIFIER = 1;
        return player.getLevel() * POINTS_MODIFIER;
    }

    @Transactional
    public List<Spell> getSpellsByPlayer(Player player) {
        return player.getCharacteristics().stream()
                .flatMap(playerCharacteristics -> playerCharacteristics.getSpells().stream())
                .map(SpellWrapper::getSpell)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Player> findPlayersByUser(User user) {
        return playerDao.findPlayersByUser(user);
    }
}
