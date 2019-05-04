package kubys.model;

import kubys.model.common.Position;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.security.Principal;


@Component
@Data
@Slf4j
public class Map {

    private HashMap<Position, Cell> cells;
    private HashMap<String, Player> mapOfPlayer;

    public Map(){
        this.mapOfPlayer = new HashMap<>();
        this.cells = new HashMap<>();
        generateEmptyMap(Position.builder()
                .x(1)
                .y(5)
                .z(1)
                .build());
    }


    public void generateEmptyMap(Position position){
        for(int x = 0; x < position.getX(); x++){
            for(int z = 0; z < position.getZ(); z++){
                this.cells.put(Position.builder()
                        .x(x)
                        .y(0)
                        .z(z)
                        .build(), LandPlot.builder().build());
            }
        }
        this.cells.forEach(((p,cell) -> cell.setPosition(p)));
    }

    public void addCell(Position position, Cell cell){
        this.cells.put(position, cell);
    }

    public Position addPlayer(Player player, Position position){
        log.debug("New player in"+cells.toString());
        if(cells.containsKey(position)) addPlayer(player, Position.builder().
                x(position.getX()+1)
                .y(position.getY())
                .z(position.getZ()).build());
        else addCell(position, player);
        player.setPosition(position);
        return position;
    }


}
