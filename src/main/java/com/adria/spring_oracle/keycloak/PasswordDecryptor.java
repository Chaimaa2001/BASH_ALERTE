package com.adria.spring_oracle.keycloak;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class PasswordDecryptor {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding"; // Mode CBC avec padding PKCS5
    private static final byte[] SECRET_KEY = "1234567891234567".getBytes(); // Clé secrète de 16 octets
    private static final byte[] IV = new byte[16]; // Initialisation du vecteur de 16 octets (IV)

    public static String decrypt(String encryptedPassword) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV); // Utilisation d'un vecteur d'initialisation

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decodedPassword = Base64.getDecoder().decode(encryptedPassword);
        byte[] originalPassword = cipher.doFinal(decodedPassword);

        return new String(originalPassword);
    }
}
