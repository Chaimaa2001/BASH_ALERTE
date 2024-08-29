package com.adria.spring_oracle.keycloak;

import com.adria.spring_oracle.entities.BackOffice;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class KeycloakUserImporter {

    private static final String SERVER_URL = "http://localhost:8080";
    private static final String REALM = "batch-alerte";
    private static final String CLIENT_ID = "Batch-BackOffice";
    private static final String ADMIN_USERNAME = "test";
    private static final String ADMIN_PASSWORD = "test";

    private Keycloak keycloak;

    public KeycloakUserImporter() {
        // Utilisez le flux password pour obtenir un token admin
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM)
                .clientId(CLIENT_ID)
                .grantType(OAuth2Constants.PASSWORD)
                .username(ADMIN_USERNAME)
                .password(ADMIN_PASSWORD)
                .build();
    }

    public void importUsers(List<BackOffice> users) {
        for (BackOffice user : users) {
            UserRepresentation userRep = new UserRepresentation();
            userRep.setUsername(user.getUsername());
            userRep.setEmail(user.getUsername() + "@example.com");
            userRep.setEnabled(true);

            try {
                String decryptedPassword = PasswordDecryptor.decrypt(user.getPassword());

                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(decryptedPassword);
                credential.setTemporary(false);
                userRep.setCredentials(Collections.singletonList(credential));

                Response response = keycloak.realm(REALM).users().create(userRep);
                if (response.getStatus() != 201) {
                    System.out.println("Failed to create user: " + response.getStatus());
                } else {
                    System.out.println("User created successfully!");
                }
            } catch (Exception e) {
                System.err.println("Error importing user: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}