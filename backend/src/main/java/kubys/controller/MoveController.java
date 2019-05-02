package kubys.controller;

import kubys.model.common.Direction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class MoveController {

//TODO : Let's move the player!

    //When a player make a direction, only return the diff with previous map
    @MessageMapping("/move")
    @SendTo("/broker/move")
    public void directionPlayer(Direction direction) {
        log.info("Server side : "+ direction.toString());

//        switch(direction){
//            case FORWARD:
//                ...
//        }
    }

}