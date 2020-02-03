package kubys.Map.Model;

import kubys.Map.Position;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class FightMap extends Map {

    public FightMap() {
        super(10, 10, 10);
        startingPositions = Set.of(
                Position.of(4, 1, 3),
                Position.of(5, 1, 3),
                Position.of(-4, 1, -3),
                Position.of(-5, 1, -3)
        );
        this.setName("Fight map");
    }

    @Override
    public void initMap() {
        super.initMap();
        LandPlot.LandPlotBuilder lpb = LandPlot.builder();

        this.addCell(Position.of(-10, 1, -10), lpb.build());
        this.addCell(Position.of(-10, 1, -9), lpb.build());
        this.addCell(Position.of(-10, 2, -9), lpb.build());
        this.addCell(Position.of(-9, 2, -9), lpb.build());
        this.addCell(Position.of(-9, 3, -9), lpb.build());
        this.addCell(Position.of(-9, 2, -8), lpb.build());

        this.addCell(Position.of(0, 1, 0), lpb.build());
        this.addCell(Position.of(0, 2, 0), lpb.build());
        this.addCell(Position.of(0, 3, 0), lpb.build());
        this.addCell(Position.of(0, 2, -1), lpb.build());
        this.addCell(Position.of(0, 1, -2), lpb.build());

        this.addCell(Position.of(3, 1, 0), lpb.build());
        this.addCell(Position.of(3, 2, 0), lpb.build());
        this.addCell(Position.of(3, 3, 0), lpb.build());
        this.addCell(Position.of(3, 4, 0), lpb.build());
        this.addCell(Position.of(3, 5, 0), lpb.build());

        this.addCell(Position.of(4, 1, 4), lpb.build());
        this.addCell(Position.of(3, 1, 4), lpb.build());
        this.addCell(Position.of(4, 1, 3), lpb.build());

        this.addCell(Position.of(-4, 1, -4), lpb.build());
        this.addCell(Position.of(-4, 2, -4), lpb.build());
        this.addCell(Position.of(-4, 3, -4), lpb.build());
        this.addCell(Position.of(-4, 4, -4), lpb.build());
        this.addCell(Position.of(-4, 5, -4), lpb.build());
        this.addCell(Position.of(-4, 6, -4), lpb.build());
        this.addCell(Position.of(-4, 7, -4), lpb.build());
        this.addCell(Position.of(-4, 8, -4), lpb.build());
        this.addCell(Position.of(-4, 9, -4), lpb.build());
        this.addCell(Position.of(-4, 10, -4), lpb.build());
    }

}
