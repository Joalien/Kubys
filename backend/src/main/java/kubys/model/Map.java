package kubys.model;

import kubys.model.common.Position;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Data
public class Map {

    private HashMap<Position, Cell> cells;

    public Map(){
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
    }

    public void addCell(Position position, Cell cell){
        this.cells.put(position, cell);
    }

    public void addPlayer(Player player, Position position){
        addCell(position, player);
        player.setPosition(position);
    }


}
