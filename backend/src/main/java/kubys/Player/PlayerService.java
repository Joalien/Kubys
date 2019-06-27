package kubys.Player;

import kubys.Map.Map;
import kubys.Map.Command;
import kubys.Map.Position;
import kubys.User.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Slf4j
public class PlayerService {

    static private Map map;
    static private SimpMessagingTemplate template;
    private PlayerDao playerDao;

    @Autowired
    public PlayerService(SimpMessagingTemplate template, Map map, PlayerDao playerDao) {
        PlayerService.template = template;
        PlayerService.map = map;
        this.playerDao = playerDao;
    }


    public void save(Player player){
        playerDao.save(player);
    }

    public static Boolean movePlayer(Player player, Command command){
        return switch(command) {
            case FORWARD -> PlayerService.moveForward(player);
            case BACKWARD -> PlayerService.moveBackward(player);
            case LEFT -> PlayerService.moveLeft(player);
            case RIGHT -> PlayerService.moveRight(player);
            case CREATE -> {
                map.addPlayer(player, Position.builder().y(5).build());
                break movePosition(player, player.getPosition());
            }
            default -> throw new IllegalStateException("Unexpected value: " + command);
        };
    }

    private static Boolean moveForward(Player player){
        Position position = player.getPosition().addZ(1);
        return movePosition(player, position);
    }

    private static Boolean moveBackward(Player player){
        Position position = player.getPosition().addZ(-1);

        return movePosition(player, position);
    }

    private static Boolean moveLeft(Player player){
        Position position = player.getPosition().addX(-1);

        return movePosition(player, position);
    }

    private static Boolean moveRight(Player player){
        Position position = player.getPosition().addX(1);

        return movePosition(player, position);
    }

    private static Boolean movePosition(Player player, Position position){// This method contains logic
        if (position == null) return false;
        Position above = position.addY(1);
        Position below = position.addY(-1);

        if(!map.getCells().containsKey(position) || map.getCells().get(position).equals(player)){
            // Drop the ~~mic~~ player
            while (!map.getCells().containsKey(below)){
                below = below.addY(-1);

            }
            updatePosition(player, below.addY(1));// Change map information
            return true;

        }else if (!map.getCells().containsKey(above) && !map.getCells().containsKey(player.getPosition().addY(1))){
            updatePosition(player, above);
            return true;
        }else return false;
    }

    private static void updatePosition(Player player, Position position){

        map.getCells().remove(player.getPosition());
        map.getCells().put(position, player);

        if(map.getCells().containsKey(player.getPosition().addY(1)) &&
                map.getCells().get(player.getPosition().addY(1)) instanceof Player) { // If they were a player on the cel above
            movePosition((Player) map.getCells().get(player.getPosition().addY(1)), player.getPosition());
            PlayerService.template.convertAndSend("/broker/command", map.getCells().get(player.getPosition()));
        }

        player.setPosition(position);
    }

}
