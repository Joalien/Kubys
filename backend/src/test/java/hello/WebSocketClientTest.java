package hello;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Create WebSocket STOMP client")
class WebSocketClientTest {
    private Logger logger = LogManager.getLogger(MyStompSessionHandler.class);


    @Test
    void createClient() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect("ws://localhost:8080/connect", sessionHandler);

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }
    private class MyStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            logger.info("New session established : " + session.getSessionId());
            session.subscribe("/simple-broker/subscription1", this);
            logger.info("Subscribed to /simple-broker/subscription1");
            session.send("/app/endpoint", getSampleMessage());
            logger.info("Message sent to /app/endpoint");
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            logger.error("Got an exception", exception);
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return HelloMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            HelloMessage msg = (HelloMessage) payload;
            logger.info("Received : " + msg.getName());
            logger.info("Received : " + msg);
            assertThat(msg.getName()).isEqualTo(getSampleMessage().getName().toUpperCase());
        }

        /**
         * A sample message instance.
         * @return instance of <code>Message</code>
         */
        private HelloMessage getSampleMessage() {
            HelloMessage msg = new HelloMessage();
            msg.setName("Joalien");
            return msg;
        }
    }
}