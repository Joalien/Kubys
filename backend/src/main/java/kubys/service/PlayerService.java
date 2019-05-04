package kubys.service;

import kubys.model.Map;
import kubys.model.Player;
import kubys.model.common.Direction;
import kubys.model.common.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlayerService {

    
    static private Map map;
    
    @Autowired
    public PlayerService(Map _map) {
        map = _map;
    }

    public static Boolean movePlayer(Player player, Direction direction){
        Boolean isMovePossible = null;
        switch(direction){
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
            default:
                log.error("Not Implemented");
                System.exit(1);
        }
        return isMovePossible;
    }

    private static Boolean moveForward(Player player){
        Position position = Position.builder()
                .x(player.getPosition().getX())
                .y(player.getPosition().getY())
                .z(player.getPosition().getZ()+1)
                .build();
        return movePosition(player, position);
    }

    private static Boolean moveBackward(Player player){
        Position position = Position.builder()
                .x(player.getPosition().getX())
                .y(player.getPosition().getY())
                .z(player.getPosition().getZ()-1)
                .build();
        return movePosition(player, position);
    }

    private static Boolean moveLeft(Player player){
        Position position = Position.builder()
                .x(player.getPosition().getX()-1)
                .y(player.getPosition().getY())
                .z(player.getPosition().getZ())
                .build();
        return movePosition(player, position);
    }

    private static Boolean moveRight(Player player){
        Position position = Position.builder()
                .x(player.getPosition().getX()+1)
                .y(player.getPosition().getY())
                .z(player.getPosition().getZ())
                .build();
        return movePosition(player, position);
    }

    private static Boolean movePosition(Player player, Position position){
        if(!map.getCells().containsKey(position)){
            //Change map information
            map.getCells().remove(player.getPosition());
            map.getCells().put(position, player);

            player.setPosition(position);
            log.debug(map.getCells().toString());
            return true;
        }else return false;
    }
}
