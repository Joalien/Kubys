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
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(KEY_PATH).getInputStream()))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.error(KEY_PATH + " file not found.");
            log.error("That's why you won't be able to use it to communicate with any client. Nevertheless, the app is not crashing so that it can be use in development environment.");
        }

        SpringApplication.run(Application.class, args);

        /*
        TODO : (MVP)
        - Optimize payload (low)
        - Use Spring Cloud Vault (low)
        - Change scene in BabylonJS
            - Refactor JS (ongoing)
            - Refactor Ring Selection with Scene switch (low)
         */
    }
}