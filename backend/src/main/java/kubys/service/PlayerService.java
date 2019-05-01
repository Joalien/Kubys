package kubys.service;

import kubys.model.Player;
import kubys.model.Position;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    public static boolean moveForward(Player player){
        Position position = Position.builder()
                .x(player.getPosition().getX())
                .y(player.getPosition().getY())
                .z(player.getPosition().getZ()+1)
                .build();
        return movePosition(player, position);
    }

    public static boolean moveBackward(Player player){
        Position position = Position.builder()
                .x(player.getPosition().getX())
                .y(player.getPosition().getY())
                .z(player.getPosition().getZ()-1)
                .build();
        return movePosition(player, position);
    }

    public static boolean moveLeft(Player player){
        Position position = Position.builder()
                .x(player.getPosition().getX()-1)
                .y(player.getPosition().getY())
                .z(player.getPosition().getZ())
                .build();
        return movePosition(player, position);
    }

    public static boolean moveRight(Player player){
        Position position = Position.builder()
                .x(player.getPosition().getX()+1)
                .y(player.getPosition().getY())
                .z(player.getPosition().getZ())
                .build();
        return movePosition(player, position);
    }

    private static boolean movePosition(Player player, Position position){
        if(!player.getCurrentMap().getCells().containsKey(position)){
            //Allow to move
            // Change player
            player.setPosition(position);
            //Change map information
            player.getCurrentMap().getCells().remove(player.getPosition());
            player.getCurrentMap().getCells().put(position, player);
            return true;
        }else return false;
    }
}
