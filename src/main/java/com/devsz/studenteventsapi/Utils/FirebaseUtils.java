package com.devsz.studenteventsapi.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.devsz.studenteventsapi.dto.UserRequest;
import com.devsz.studenteventsapi.firebase.FirebaseInitializer;
import com.google.cloud.firestore.CollectionReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FirebaseUtils {
    private static final List<String> VALID_ROLES = Arrays.asList("STUDENT", "ADMIN", "DEV");
    private static final String ROLES_CLAIM = "role";

    public static CollectionReference getCollectionReference(FirebaseInitializer fireInit, String... pathSegments) {
        CollectionReference collectionRef = fireInit.getFirestore().collection(pathSegments[0]);
        for (int i = 1; i < pathSegments.length; i++) {
            collectionRef = collectionRef.document(pathSegments[i]).collection(pathSegments[++i]);
        }
        return collectionRef;
    }

    public static void validateRoles(String roles) {
        if (!VALID_ROLES.contains(roles.toUpperCase())) {
            throw new IllegalArgumentException("Invalid roles: " + roles);
        }
    }

    public static String generateCustomToken(UserRequest user, String uid) throws FirebaseAuthException {
        // Añadir roles como custom claims
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put(ROLES_CLAIM, user.getRole());
        FirebaseAuth.getInstance().setCustomUserClaims(uid, customClaims);

        // Generar un token de intercambio personalizado
        return FirebaseAuth.getInstance().createCustomToken(uid, customClaims);
    }

    public static boolean checkOwnership(String dataId, String ownerId) {
        return dataId.equals(ownerId);
    } 

}
