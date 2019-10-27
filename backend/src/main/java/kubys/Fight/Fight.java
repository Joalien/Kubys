package kubys.Fight;


import kubys.Player.Player;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Slf4j
@Builder
@Data
public class Fight {
    private Long id;

    List<Player> players;
    //TODO add custom map pool
}
