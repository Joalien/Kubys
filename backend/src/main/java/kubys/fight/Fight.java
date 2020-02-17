package kubys.fight;


import kubys.map.Map;
import kubys.player.Player;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Builder
@Data
class Fight {
    private String uuid;

    private Map map;
    private List<Player> players;
}
