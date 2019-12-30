package kubys.Map;

import kubys.Player.Player;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;


@Component
@Data
@Slf4j
public class Map {

    private ConcurrentHashMap<Position, Cell> cells;
    public static final int MAX_X_SIZE = 10;
    public static final int MAX_Y_SIZE = 10;
    public static final int MAX_Z_SIZE = 10;

    public Map() {
        this.cells = new ConcurrentHashMap<>();
    }


    private void generateFightMap1() {
        log.debug("Generate Fight map");
        LandPlot.LandPlotBuilder lpb = LandPlot.builder();

        generateEmptyMap(MAX_X_SIZE, MAX_Z_SIZE);

        addCell(Position.of(-10, 1, -10), lpb.build());
        addCell(Position.of(-10, 1, -9), lpb.build());
        addCell(Position.of(-10, 2, -9), lpb.build());
        addCell(Position.of(-9, 2, -9), lpb.build());
        addCell(Position.of(-9, 3, -9), lpb.build());
        addCell(Position.of(-9, 2, -8), lpb.build());

        addCell(Position.of(0, 1, 0), lpb.build());
        addCell(Position.of(0, 2, 0), lpb.build());
        addCell(Position.of(0, 3, 0), lpb.build());
        addCell(Position.of(0, 2, -1), lpb.build());
        addCell(Position.of(0, 1, -2), lpb.build());

        addCell(Position.of(3, 1, 0), lpb.build());
        addCell(Position.of(3, 2, 0), lpb.build());
        addCell(Position.of(3, 3, 0), lpb.build());
        addCell(Position.of(3, 4, 0), lpb.build());
        addCell(Position.of(3, 5, 0), lpb.build());

        addCell(Position.of(4, 1, 4), lpb.build());
        addCell(Position.of(3, 1, 4), lpb.build());
        addCell(Position.of(4, 1, 3), lpb.build());

        addCell(Position.of(-4, 1, -4), lpb.build());
        addCell(Position.of(-4, 2, -4), lpb.build());
        addCell(Position.of(-4, 3, -4), lpb.build());
        addCell(Position.of(-4, 4, -4), lpb.build());
        addCell(Position.of(-4, 5, -4), lpb.build());
        addCell(Position.of(-4, 6, -4), lpb.build());
        addCell(Position.of(-4, 7, -4), lpb.build());
        addCell(Position.of(-4, 8, -4), lpb.build());
        addCell(Position.of(-4, 9, -4), lpb.build());
        addCell(Position.of(-4, 10, -4), lpb.build());
    }

    public void generateEmptyMap(int sizeX, int sizeZ) {
        for(int x = -sizeX; x <= sizeX; x++) {
            for(int z = -sizeZ; z <= sizeZ; z++) {
                addCell(Position.of(x, 0, z), LandPlot.builder().build());
            }
        }
    }

    private void addCell(Position position, Cell cell) {
        try{
            this.cells.put(position, cell);
            cell.setPosition(position);
        } catch (NullPointerException ignored) {}
    }

    public Position addPlayer(Player player, Position position) {
//        log.debug("New player in "+cells.toString());
        if(cells.containsKey(position)) addPlayer(player, position.plusY(1));
        else addCell(position, player);
        return position;
    }

    public String toString() {
        this.getCells().entrySet().parallelStream().map(java.util.Map.Entry::getKey).forEach(x -> System.out.println(x + " : " + this.cells.get(x)));
        return "";
    }

}
