package kubys;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class Application {

    private static final String KEY_PATH = "serviceAccountPrivateKey.json";

    public static void main(String[] args) throws IOException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(KEY_PATH).getInputStream()))
                .build();
        FirebaseApp.initializeApp(options);

        SpringApplication.run(Application.class, args);

        /*
        TODO : (MVP)
        - Optimize payload (low)
        - Use Spring Cloud Vault (low)
        - Make tests run (low)
        - Change scene in BabylonJS
            - Refactor JS (low)
            - Refactor Ring Selection with Scene switch (low)
            - Add new Fight Scene (high)
        - Add characteristic tree
         */
    }
}