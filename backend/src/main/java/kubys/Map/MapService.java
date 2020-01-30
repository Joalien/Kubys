package kubys.Map;

import kubys.Map.Model.FightMap;
import kubys.Map.Model.MainMap;
import kubys.Map.Model.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Data
@Slf4j
public class MapService { // Store all map instances
    public static final Map MAIN_MAP;

    static {
        // Generate the main map here
        MAIN_MAP = new MainMap();
        MAIN_MAP.initMap();
    }

    public static Map generateFightMap() {
        Map map = new FightMap();
        map.initMap();

        return map;
    }
}
