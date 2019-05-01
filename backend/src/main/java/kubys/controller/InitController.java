package kubys.controller;

import kubys.model.Map;
import kubys.model.Player;
import kubys.model.Position;
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
    public Map initMap() {
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
                .x(5)
                .y(3)
                .z(5)
                .build(), mainMap);
        MapService.addPlayer(mainMap, player, Position.builder()
                .x(0)
                .y(1)
                .z(0)
                .build());
        
        
        log.info("Server side : "+ mainMap);
        return mainMap;
    }

}