package kubys.controller;

import kubys.model.Cell;
import kubys.model.Player;
import kubys.model.Position;
import kubys.protocol.Move;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@Slf4j
public class MoveController {


//    @MessageMapping("/move")
//    @SendTo("/broker/mainMap")
//    public Map<Position, Cell> movePlayer(Move move) {
//        Player m = Player.builder().build();
//        m.setName(player.getName().toUpperCase());
//        log.info("Server side : "+ m.getName());
//        return m;
//    }

}