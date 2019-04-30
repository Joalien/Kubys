package hello;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {



    @MessageMapping("/endpoint")
    @SendTo("/simple-broker/subscription1")
    public HelloMessage greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        HelloMessage m = new HelloMessage();
        m.setName(message.getName().toUpperCase());
        return m;
    }

}