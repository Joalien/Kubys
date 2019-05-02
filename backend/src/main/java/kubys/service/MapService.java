package kubys.service;

import kubys.model.*;
import kubys.model.common.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Component
public class MapService {

    private Map map;

    @Autowired
    public MapService(){
        this.map = new Map();
        generateEmptyMap(Position.builder()
                .x(1)
                .y(5)
                .z(1)
                .build());
    }


    public void generateEmptyMap(Position position){
        this.map = Map.builder().cells(new HashMap<>()).build();
        for(int x = 0; x < position.getX(); x++){
            for(int z = 0; z < position.getZ(); z++){
                this.map.getCells().put(Position.builder()
                        .x(x)
                        .y(0)
                        .z(z)
                        .build(), LandPlot.builder().build());
            }
        }
    }

    public void addCell(Position position, Cell cell){
        this.map.getCells().put(position, cell);
    }

    public void addPlayer(Player player, Position position){
        addCell(position, player);
        player.setPosition(position);
    }

    public Map getMap() {
        return this.map;
    }
}
