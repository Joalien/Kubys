package kubys.map;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@Slf4j
public class MainMap extends Map {

    MainMap() {
        super(10, 10, 10);
        Position startingPosition = Position.of(0, 4, 0);
        startingPositions = Set.of(
                startingPosition,
                startingPosition.plusY(1),
                startingPosition.plusY(2),
                startingPosition.plusY(3));
        this.setName("Main map");
    }

    @Override
    public void initMap() {
        super.initMap();
        LandPlot.LandPlotBuilder lpb = LandPlot.builder();

        this.addCell(Position.of(0, 1, 10), lpb.build());
    }

}
