package com.grudus.configuration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.grudus.entities.User;
import com.grudus.helpers.JsonHelper;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class TokenHandler {

    private final Mac hmac;
    private static final String HMAC_ALGO = "HmacSHA256";

    public static final String SEPARATOR = ".";
    public static final String SEPARATOR_SPLITTER = "\\.";

    public TokenHandler(byte[] key) {
        try {
            hmac = Mac.getInstance(HMAC_ALGO);
            hmac.init(new SecretKeySpec(key, HMAC_ALGO));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("failed to initialize HMAC" + e.getMessage(), e);
        }

    }


    private synchronized byte[] createHmac(byte[] content) {
        return hmac.doFinal(content);
    }

    private String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private byte[] fromBase64(String decoded) {
        return Base64.getDecoder().decode(decoded);
    }

    public String createTokenForUser(User user) throws UnsupportedEncodingException, JsonProcessingException {
        byte[] userBytes = JsonHelper.userToJsonBytes(user);
        byte[] hash = createHmac(userBytes);

        final StringBuilder builder = new StringBuilder(170);
        builder.append(toBase64(userBytes))
                .append(SEPARATOR)
                .append(toBase64(hash));

        return builder.toString();
    }

    public User parseUserFromToken(String token) throws IOException {
        final String[] parts = token.split(SEPARATOR_SPLITTER);
        if (inputIsValid(parts)) {
            final byte[] userBytes = fromBase64(parts[0]);
            final byte[] hash = fromBase64(parts[1]);

            boolean validHash = Arrays.equals(createHmac(userBytes), hash);

            if (validHash) {
                return JsonHelper.fromJsonToUser(userBytes);
            }
        }
        return null;
    }

    private boolean inputIsValid(String[] input) {
        return input.length == 2 && input[0].length() > 0 && input[1].length() > 0;
    }

}
