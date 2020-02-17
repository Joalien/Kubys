package kubys.map;

import kubys.player.Player;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
@Slf4j
public abstract class Map {

    private java.util.Map<Position, Cell> cells = new ConcurrentHashMap<>();

    private int sizeX;
    private int sizeY;
    private int sizeZ;
    protected Set<Position> startingPositions;
    private String name;

    protected Map(int x, int y, int z) {
        // Protected cstor to allow inheritance
        sizeX = x;
        sizeY = y;
        sizeZ = z;
    }

    public void createGround() {
        for(int x = -sizeX; x <= sizeX; x++) {
            for(int z = -sizeZ; z <= sizeZ; z++) {
                this.addCell(Position.of(x, 0, z), LandPlot.builder().build());
            }
        }
    }

    public final void addCell(Position position, Cell cell) {
        if (!this.isInsideMap(position)) throw new IndexOutOfBoundsException();
        this.cells.put(position, cell);
        cell.setPosition(position);

    }

    public final Cell removeCell(Position position) {
        return this.cells.remove(position);
    }

    // Allow each map to define its own starting map
    public void initMap() {
        this.createGround();
    }

    public boolean isInsideMap(Position position) {
        return Math.abs(position.getX()) <= this.sizeX
                && Math.abs(position.getY()) <= this.sizeY
                && Math.abs(position.getZ()) <= this.sizeY;
    }

    @Override
    public String toString() {
        return this.name + this.cells.entrySet().stream().filter(positionCellEntry -> positionCellEntry.getValue() instanceof Player).collect(Collectors.toSet()).toString();
    }
}
