package kubys.controller;

import kubys.protocol.Move;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class MoveController {

//TODO : Let player move !

    //When a player make a move, only return the diff with previous map
    @MessageMapping("/move")
    @SendTo("/broker/mainMap")
    public void movePlayer(Move move) {
        log.info("Server side : "+ move.toString());
    }

}