package kubys.Map.Model;

import kubys.Map.Position;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@Slf4j
public class MainMap extends Map {

    public MainMap() {
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
        // TODO create beautiful main map and fun fight map !
    }

}
