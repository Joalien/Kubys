package kubys.service;

import kubys.model.Map;
import kubys.model.Player;
import kubys.model.common.Command;
import kubys.model.common.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlayerService {

    
    static private Map map;
    static private SimpMessagingTemplate template;

    @Autowired
    public PlayerService(SimpMessagingTemplate template, Map map) {
        PlayerService.template = template;
        PlayerService.map = map;
    }

    public static Boolean movePlayer(Player player, Command command){
        Boolean isMovePossible = null;
        switch(command){
            case FORWARD:
                isMovePossible = PlayerService.moveForward(player);
                break;
            case BACKWARD:
                isMovePossible = PlayerService.moveBackward(player);
                break;
            case LEFT:
                isMovePossible = PlayerService.moveLeft(player);
                break;
            case RIGHT:
                isMovePossible = PlayerService.moveRight(player);
                break;
            case CREATE:
                map.addPlayer(player, Position.builder().y(1).build());
                isMovePossible = true;
                break;
            default:
                log.error("Not Implemented");
                System.exit(1);
        }
        return isMovePossible;
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
        Position above = position.addY(1);
        Position below = position.addY(-1);

        if(!map.getCells().containsKey(position)){
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

//        log.error(player.getPosition().toString());

        if(map.getCells().containsKey(player.getPosition().addY(1)) &&
            map.getCells().get(player.getPosition().addY(1)) instanceof Player) { // If they were a player on the cel above
            movePosition((Player) map.getCells().get(player.getPosition().addY(1)), player.getPosition());
            PlayerService.template.convertAndSend("/broker/command", map.getCells().get(player.getPosition()));
        }



        player.setPosition(position);
    }

}
