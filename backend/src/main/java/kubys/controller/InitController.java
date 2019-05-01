package kubys.controller;

import kubys.model.Cell;
import kubys.model.Map;
import kubys.model.Player;
import kubys.model.common.Breed;
import kubys.model.common.Position;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class InitController {

    //WARNING, THIS IS NOT RESTFULL AT ALL, SINGLETONS HAS INSTANCE VARIABLE !!!
    private Map map;

    private static int from = 0;

    @Autowired
    public InitController(Map map) {
        this.map = map;
    }

    @MessageMapping("/getAllMap")
    @SendToUser("/broker/getAllMap")
    public Cell[] initMap() {

        Player player = Player.builder()
                .breed(Breed.DWARF)
                .currentMap(this.map)
                .level(1)
                .name("Joalien")
                .pa(10)
                .pm(5)
                .build();

        log.debug("Before : "+map.getCells().toString());

        this.map.addPlayer(player, Position.builder()
                .x(from++)
                .y(1)
                .z(0)
                .build());

        log.debug("After : "+map.getCells().toString());

        map.getCells()
                .forEach(((position, cell) -> cell.setPosition(position)));

        return map.getCells().values().toArray(new Cell[0]);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        log.error(exception.toString());
        return exception.getMessage();
    }

}