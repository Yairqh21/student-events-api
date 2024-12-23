package com.devsz.studenteventsapi.Utils;

import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.google.cloud.firestore.CollectionReference;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FirestoreUtils {

    public static CollectionReference getCollectionReference(FirebaseInitializer fireInit, String... pathSegments) {
        CollectionReference collectionRef = fireInit.getFirestore().collection(pathSegments[0]);
        for (int i = 1; i < pathSegments.length; i++) {
            collectionRef = collectionRef.document(pathSegments[i]).collection(pathSegments[++i]);
        }
        return collectionRef;
    }

    // public static DocumentReference getDocumentReference( FirebaseInitializer fireInit, String... pathSegments) {
    // DocumentReference docRef =
    // fireInit.getFirestore().collection(pathSegments[0]).document(pathSegments[1]);
    // for (int i = 2; i < pathSegments.length; i++) {
    // docRef = docRef.collection(pathSegments[i]).document(pathSegments[++i]);
    // }
    // return docRef;
    // }

}
