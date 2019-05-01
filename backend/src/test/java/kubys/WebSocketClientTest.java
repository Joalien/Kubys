package hello;

import hello.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

@SpringBootTest(classes = Application.class)
@DisplayName("Test WebSocket STOMP client")
@AutoConfigureMockMvc
@Slf4j
class WebSocketClientTest {

    @Test
    @DisplayName("Exchange simple message with server")
    void setup() {

        StompSession session1 = createSession();
        session1.subscribe("/broker/subscription1", new MyStompFrameHandler());
        StompSession session2 = createSession();
        session2.subscribe("/broker/subscription1", new MyStompFrameHandler());
        Player msg =  Player.builder().build();
        msg.setName("new Test session 1");
        session1.send("/endpoint", msg);



        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TODO: Improve test
        assert true;
    }

    StompSession createSession(){
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter(){};
        try {
            return stompClient.connect("ws://localhost:8080/connect", sessionHandler).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Unable to connect to server");
            assert false;
        }
        return null;
    }

    private class MyStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return Player.class;
        }

        @Override
        public void handleFrame(StompHeaders headers,Object payload) {
            log.info(((Player)payload).getName());
        }
    }


}