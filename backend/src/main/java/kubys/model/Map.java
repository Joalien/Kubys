package kubys.model;

import kubys.model.common.Position;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.security.Principal;


@Component
@Data
public class Map {

    private HashMap<Position, Cell> cells;
    private HashMap<String, Player> mapOfPlayer;

    public Map(){
        this.mapOfPlayer = new HashMap<>();
        this.cells = new HashMap<>();
        generateEmptyMap(Position.builder()
                .x(0)
                .y(5)
                .z(0)
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

    public void addPlayer(Player player, Position position){
        if(cells.containsKey(position)) addPlayer(player, Position.builder().
                x(position.getX())
                .y(position.getY()+1)
                .z(position.getZ()+1).build());
        else addCell(position, player);
        player.setPosition(position);
    }


}
