package kubys.controller;

import kubys.model.Cell;
import kubys.model.Player;
import kubys.model.common.Breed;
import kubys.model.common.Position;
import kubys.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class InitController {

//WARNING, THIS IS NOT RESTFULL AT ALL, SINGLETONS HAS INSTANCE VARIABLE !!!
private MapService mapService;

    private static int from = 0;

    @Autowired
    public InitController(MapService mapService) {
        this.mapService = mapService;
    }

    @MessageMapping("/getAllMap")
    @SendTo("/broker/getAllMap")
    public Cell[] initMap() {

        Player player = Player.builder()
                .breed(Breed.DWARF)
                .currentMap(this.mapService.getMap())
                .level(1)
                .name("Joalien")
                .pa(10)
                .pm(5)
                .build();

//        log.info("Before : "+mapService.getMap().getCells().toString());

        this.mapService.addPlayer(player, Position.builder()
                .x(from++)
                .y(1)
                .z(0)
                .build());

//        log.info("After : "+mapService.getMap().getCells().toString());

        mapService.getMap().getCells()
                .forEach(((position, cell) -> cell.setPosition(position)));

        return mapService.getMap().getCells().values().toArray(new Cell[0]);
    }

}