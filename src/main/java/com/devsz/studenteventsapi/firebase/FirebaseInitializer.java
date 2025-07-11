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
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase.json")) {
            if (serviceAccount == null) {
                throw new FileNotFoundException("File not found: private_key_firebase.json");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://appeventos-6bbfc.firebaseio.com")
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
