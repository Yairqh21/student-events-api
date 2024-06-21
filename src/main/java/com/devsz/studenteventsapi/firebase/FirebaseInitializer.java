package com.devsz.studenteventsapi.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class FirebaseInitializer {

    @PostConstruct
    public void initFirestore() {
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("private_key_firebase.json")) {
            if (serviceAccount == null) {
                throw new FileNotFoundException("File not found: private_key_firebase.json");
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://events-b4221.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    public Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }
}
