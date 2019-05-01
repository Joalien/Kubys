package hello;

import hello.model.*;
import hello.service.MapService;
import hello.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
@Slf4j
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        Map mainMap = Map.builder().cells(new HashMap<>()).build();
        Player player = Player.builder()
                .breed(Breed.Dwarf)
                .currentMap(mainMap)
                .level(1)
                .name("Joalien")
                .pa(10)
                .pm(5)
                .build();

        MapService.generateEmptyMap(Position.builder()
                .x(2)
                .y(2)
                .z(2)
                .build(), mainMap);
        MapService.addPlayer(mainMap, player, Position.builder()
                .x(0)
                .y(1)
                .z(0)
                .build());
        PlayerService.moveForward(player);

        log.info(mainMap.toString());
    }
}