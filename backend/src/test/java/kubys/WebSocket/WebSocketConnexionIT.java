package kubys.WebSocket;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import kubys.Player.Player;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test WebSocket STOMP client")
@Slf4j
class WebSocketConnexionIT {

    private static final String KEY_PATH = "serviceAccountPrivateKey.json";
    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Verify that credentials file exists")
    void testCredentialFilePresence() {
        assertTrue(new ClassPathResource(KEY_PATH).exists());
    }

    @Disabled
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

    private StompSession createSession() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {};
        String token = createToken();

        try {
            StompHeaders headers = new StompHeaders();
            headers.add("login", token);
            return stompClient.connect("ws://127.0.0.1:" + port + "/connect", (WebSocketHttpHeaders) null, headers, sessionHandler).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    private String createToken() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(KEY_PATH).getInputStream()))
                    .build();
            FirebaseApp.initializeApp(options);

            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail("josquin.cornec@kubys.fr");
            return FirebaseAuth.getInstance().createCustomTokenAsync(userRecord.getUid()).get();
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
        }
    }
}