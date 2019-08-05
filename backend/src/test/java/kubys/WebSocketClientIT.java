package kubys;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import kubys.Player.Player;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@DisplayName("Test WebSocket STOMP client")
@AutoConfigureMockMvc
@EnableWebSocketMessageBroker
@Slf4j
class WebSocketClientIT {

    private static final String KEY_PATH = "serviceAccountPrivateKey.json";

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

    StompSession createSession() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {};
        String token = createToken();

        try {
            StompHeaders headers = new StompHeaders();
            headers.add("idToken", token);
            return stompClient.connect("ws://127.0.0.1:8443/connect", (WebSocketHttpHeaders) null, headers, sessionHandler).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    String createToken() {
        try {
            FirebaseOptions options = null;
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(KEY_PATH).getInputStream()))
                    .build();
            FirebaseApp.initializeApp(options);

            String uid = "e4T2idn6rgPGFLpUXN8vpwpNzBy2";
            return FirebaseAuth.getInstance().createCustomTokenAsync(uid).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static class MyStompFrameHandler implements StompFrameHandler {
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