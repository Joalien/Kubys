package kubys.model;

import kubys.model.common.Position;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Data
@Slf4j
public class Map {

    private ConcurrentHashMap<Position, Cell> cells;
    private HashMap<String, Player> mapOfPlayer;

    public Map(){
        this.mapOfPlayer = new HashMap<>();
        this.cells = new ConcurrentHashMap<>();
        generateFightMap1();
        generateEmptyMap(Position.builder()
                .x(10)
                .y(5)
                .z(10)
                .build());


    }


    public void generateFightMap1(){
        log.debug("Generate Fight map");
        LandPlot.LandPlotBuilder lpb = LandPlot.builder();
        Position.PositionBuilder pb = Position.builder();

        generateEmptyMap(pb.x(10).z(10).build());

        addCell(pb.x(-10).y(1).z(-10).build(), lpb.build());
        addCell(pb.x(-10).y(1).z(-9).build(), lpb.build());
        addCell(pb.x(-10).y(2).z(-9).build(), lpb.build());
        addCell(pb.x(-9).y(2).z(-9).build(), lpb.build());
        addCell(pb.x(-9).y(3).z(-9).build(), lpb.build());
        addCell(pb.x(-9).y(2).z(-8).build(), lpb.build());

        addCell(pb.x(0).y(1).z(0).build(), lpb.build());
        addCell(pb.x(0).y(2).z(0).build(), lpb.build());
        addCell(pb.x(0).y(3).z(0).build(), lpb.build());
        addCell(pb.x(0).y(2).z(-1).build(), lpb.build());
        addCell(pb.x(0).y(1).z(-2).build(), lpb.build());

        addCell(pb.x(3).y(1).z(0).build(), lpb.build());
        addCell(pb.x(3).y(2).z(0).build(), lpb.build());
        addCell(pb.x(3).y(3).z(0).build(), lpb.build());
        addCell(pb.x(3).y(4).z(0).build(), lpb.build());
        addCell(pb.x(3).y(5).z(0).build(), lpb.build());

        addCell(pb.x(4).y(1).z(4).build(), lpb.build());
        addCell(pb.x(3).y(1).z(4).build(), lpb.build());
        addCell(pb.x(4).y(1).z(3).build(), lpb.build());

        addCell(pb.x(-4).y(1).z(-4).build(), lpb.build());
        addCell(pb.x(-4).y(2).z(-4).build(), lpb.build());
        addCell(pb.x(-4).y(3).z(-4).build(), lpb.build());
        addCell(pb.x(-4).y(4).z(-4).build(), lpb.build());
        addCell(pb.x(-4).y(5).z(-4).build(), lpb.build());
        addCell(pb.x(-4).y(6).z(-4).build(), lpb.build());
        addCell(pb.x(-4).y(7).z(-4).build(), lpb.build());
        addCell(pb.x(-4).y(8).z(-4).build(), lpb.build());
        addCell(pb.x(-4).y(9).z(-4).build(), lpb.build());
        addCell(pb.x(-4).y(10).z(-4).build(), lpb.build());
        addCell(pb.x(-4).y(11).z(-4).build(), lpb.build());


    }

    public void generateEmptyMap(Position position){
        for(int x = -position.getX(); x <= position.getX(); x++){
            for(int z = -position.getZ(); z <= position.getZ(); z++){

                addCell(Position.builder()
                        .x(x)
                        .y(0)
                        .z(z)
                        .build(), LandPlot.builder().build());
            }
        }
    }

    public void addCell(Position position, Cell cell){
        cell.setPosition(position);
        this.cells.put(position, cell);
    }

    public Position addPlayer(Player player, Position position){
//        log.debug("New player in "+cells.toString());
        if(cells.containsKey(position)) addPlayer(player, Position.builder().
                x(position.getX())
                .y(position.getY()+1)
                .z(position.getZ()).build());
        else addCell(position, player);
        return position;
    }


}
