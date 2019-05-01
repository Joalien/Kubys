package kubys.controller;

import kubys.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class GreetingController {


    @MessageMapping("/endpoint")
    @SendTo("/broker/subscription1")
    public Player toUpperCase(Player player) throws Exception {
        Thread.sleep(1000); // simulated delay
        Player m = Player.builder().build();
        m.setName(player.getName().toUpperCase());
        log.info("Server side : "+ m.getName());
        return m;
    }

}