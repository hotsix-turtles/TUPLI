package hotsixturtles.tupli.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FirebaseInitialize {
    @PostConstruct
    public void initialize() throws IOException {

        InputStream resource = new ClassPathResource("serviceAccountKey.json").getInputStream();
//        FileInputStream serviceAccount =
//                new FileInputStream("./src/main/resources/serviceAccountKey.json");
//                new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/serviceAccountKey.json");

        FirebaseApp firebaseApp = null;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if(firebaseApps != null && !firebaseApps.isEmpty()) {
            for(FirebaseApp app : firebaseApps) {
                if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                    firebaseApp = app;
            }
        } else {

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(resource))
                    .setDatabaseUrl("https://tupli-339201-default-rtdb.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }
}