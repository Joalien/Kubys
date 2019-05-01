package kubys.controller;

import kubys.model.Cell;
import kubys.model.Map;
import kubys.model.Player;
import kubys.model.common.Position;
import kubys.model.common.Breed;
import kubys.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
@Slf4j
public class InitController {


    @MessageMapping("/init")
    @SendTo("/broker/mainMap")//TODO : send only to source
    public Cell[] initMap() {
        Map mainMap = Map.builder().cells(new HashMap<>()).build();
        Player player = Player.builder()
                .breed(Breed.DWARF)
                .currentMap(mainMap)
                .level(1)
                .name("Joalien")
                .pa(10)
                .pm(5)
                .build();

        MapService.generateEmptyMap(Position.builder()
                .x(30)
                .y(3)
                .z(30)
                .build(), mainMap);
        log.info("Map.size() = "+mainMap.getCells().size());
        MapService.addPlayer(mainMap, player, Position.builder()
                .x(0)
                .y(1)
                .z(0)
                .build());

        mainMap.getCells()
                .forEach(((position, cell) -> cell.setPosition(position)));
        return mainMap.getCells().values().toArray(new Cell[0]);
    }

}