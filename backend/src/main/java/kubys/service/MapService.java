package hello.service;

import hello.model.*;
import org.springframework.stereotype.Service;

@Service
public class MapService {

    public static void generateEmptyMap(Position position, Map map){
        for(int x = 0; x < position.getX(); x++){
            for(int z = 0; z < position.getY(); z++){
                map.getCells().put(Position.builder()
                        .x(x)
                        .y(0)
                        .z(z)
                        .build(), LandPlot.builder().build());
            }
        }
    }

    public static void addCell(Position position, Map map, Cell cell){
        map.getCells().put(position, cell);
    }

    public static void addPlayer(Map map, Player player, Position position){
        map.getCells().put(position, player);
        player.setPosition(position);
    }
}
