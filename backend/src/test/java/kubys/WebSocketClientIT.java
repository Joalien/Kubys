package kubys;

import kubys.model.Player;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

@SpringBootTest(classes = Application.class)
@DisplayName("Test WebSocket STOMP client")
@AutoConfigureMockMvc
@EnableWebSocketMessageBroker
@ContextConfiguration
@Slf4j
class WebSocketClientIT {

    @Test
    @DisplayName("Extremely simple exchange with server")
    void connectWebSocket() {

        StompSession session1 = createSession();
        session1.subscribe("/getAllMap", new MyStompFrameHandler());
        session1.send("/getAllMap", null);


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TODO: Improve test
        assert true;
    }


    @Test
    @DisplayName("Exchange simple message with server")
    void setup() {

        StompSession session1 = createSession();
        session1.subscribe("/broker/getAllMap", new MyStompFrameHandler());
        StompSession session2 = createSession();
        session2.subscribe("/broker/getAllMap", new MyStompFrameHandler());
        session1.send("/getAllMap", null);


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
            WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
            headers.add("login", "username");
            headers.add("passcode", "passcode");
            return stompClient.connect("ws://localhost:8080/connect", headers, sessionHandler).get();
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
//            log.info(((Player)payload).getName());
        }
    }


//
//    @Nested
//    @DisplayName("Test that users authenticate well")
//    class AuthenticationTest {
//
//        @Test
//        @DisplayName("Unauthenticated user can't connect ")
//        void getMessageUnauthenticated() {
//            assertThatThrownBy(this::getMessage).isInstanceOf(AuthenticationCredentialsNotFoundException.class);
//        }
//
//        @Test
//        @PreAuthorize("authenticated")
//        @DisplayName("Authenticated user can't connect ")
//        void getMessage() {
//            Authentication authentication = SecurityContextHolder.getContext()
//                    .getAuthentication();
//            log.debug(authentication.toString());
//            assert true;
//        }
//
//        @Test
//        @WithMockUser
//        @DisplayName("What will happend with mock user ?")
//        void getMessageWithMockUser() {
//            getMessage();
//            assert true;
//        }
//
//        @Test
//        @WithAnonymousUser
//        @DisplayName("Anonymous user can't connect ")
//        void getMessageWithAnonymousUser() {
//            assertThatThrownBy(this::getMessage).isInstanceOf(AuthenticationCredentialsNotFoundException.class);
//        }
//    }



}